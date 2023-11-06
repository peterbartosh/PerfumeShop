package com.example.perfumeshop.presentation.features.auth.login_feature.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.data.utils.UserSex
import com.example.perfumeshop.presentation.app.ui.ERROR_TAG
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val fireRepository: FireRepository
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.NotStarted())

    val uiState : StateFlow<UiState> = _uiState

    private val _auth = FirebaseAuth.getInstance()

    private var firstNameState by mutableStateOf("")
    private var secondNameState by mutableStateOf("")
    private var sexState by mutableStateOf(2)
    var phoneNumberState by mutableStateOf("")
    private var passwordState by mutableStateOf("")

    override fun onCleared() {
        Log.d("CLEARED_SOAJD", "onCleared: done")
        super.onCleared()
    }

    fun notifyAdmin(
        firstName : String, secondName : String,
        phoneNumber : String, sexInd : Int,
        pwd : String, seed : Int,
        isInBlackList : () -> Unit,
        onAdminNotified : () -> Unit,
        onError : (String) -> Unit
    ) = viewModelScope.launch {

        _uiState.value = UiState.Loading()

        if (fireRepository.isInBlackList(phoneNumber)) {
            isInBlackList()
            _uiState.value = UiState.Failure(Exception("is in black list"))
            return@launch
        }

        firstNameState = firstName
        secondNameState = secondName
        sexState = sexInd
        phoneNumberState = phoneNumber
        passwordState = pwd

        val userPhoneAlreadyInUse = fireRepository.phoneNumberIsNotUsedYet(phoneNumberState)
        if (!userPhoneAlreadyInUse){
            onError("Ошибка.\nДанный номер телефона уже зарегистрирован.")
            _uiState.value = UiState.Failure(Exception("phone num already used"))
            return@launch
        }

        val code = Random(seed).nextInt(100, 999)

        val getDeferred = async(Dispatchers.IO) {
            roomRepository.getRegistrationRequest(phoneNumber)
        }

        val existingRequest = getDeferred.await()

        var sendResult = Result.success("")

        if (existingRequest == null) {
            val emailSender = EmailSender()

            sendResult =
                emailSender.sendRegisterRequestEmail(firstName, secondName, phoneNumber, code, this)

            val insertDeferred = async(Dispatchers.IO) {
                roomRepository.addRegistrationRequest(
                    RegistrationRequestEntity(phoneNumber, code)
                )
            }

            insertDeferred.await()
        }

        if (sendResult.isSuccess) {
            _uiState.value = UiState.Success("")
            onAdminNotified()
        }
        else {
            Log.d("ERROR_ERROR", "notifyAdmin: ${sendResult.exceptionOrNull()} ${sendResult.exceptionOrNull()?.message}")
            _uiState.value = UiState.Failure(sendResult.exceptionOrNull())
            onError("Ошибка.\nПопробуйте позже.")
        }
    }

    fun verifyCode(
        code : String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading()

        val getDeferred = async(Dispatchers.IO) {
            roomRepository.getRegistrationRequest(phoneNumberState)
        }
        val existingRequest = getDeferred.await()
        if (existingRequest?.code?.toString() == code){
            _uiState.value = UiState.Success()
            onSuccess()
        }
        else {
            _uiState.value = UiState.Failure(Exception("codes arent equal"))
            onFailure()
        }

    }


    fun register() = viewModelScope.launch {

        _uiState.value = UiState.Loading()

        _auth.createUserWithEmailAndPassword(
            "$phoneNumberState@gmail.com", passwordState
        ).addOnSuccessListener {
            UserData.initializeUserData(
                firstName = firstNameState,
                secondName = secondNameState,
                phoneNumber = phoneNumberState,
                sexInd = sexState
            )
            createUserInDatabase()
        }.await()

        val clearJob = launch(Dispatchers.IO) {
            roomRepository.clearAllRegistrationRequests()
        }

        clearJob.join()

        _uiState.value = UiState.Success()
    }

    fun signIn(
        phoneNumber: String,
        password : String,
        onSuccess : () -> Unit,
        errorCallback : (String) -> Unit
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading()
        try {
            _auth.signInWithEmailAndPassword("$phoneNumber@gmail.com", password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        UserData.loadUserData()
                        _uiState.value = UiState.Success()
                        onSuccess()
                    } else {
                        Log.d(ERROR_TAG, "signIn: ${it.exception} ${it.exception?.message}")
                        _uiState.value = UiState.Failure(it.exception)
                        //errorCallback(it.exception?.message.toString() + "1")
                    }

                }.await()
        } catch (e : FirebaseException){
            Log.d(ERROR_TAG, "signIn: invalid credentials")
            _uiState.value = UiState.Failure(e)
            errorCallback("Ошибка.\nПользователь не найден.")
        } catch (e : Exception){
            Log.d(ERROR_TAG, "signIn: ${e.message}")
            _uiState.value = UiState.Failure(e)
            errorCallback(e.message.toString())
        }
        _uiState.value = UiState.Failure(Exception("not done"))
    }

    private fun createUserInDatabase() {
        viewModelScope.launch {
            val deferred = async {
                val uid = _auth.uid
                if (uid != null) {
                    val user = User(
                        id = uid,
                        firstName = firstNameState,
                        secondName = secondNameState,
                        phoneNumber = phoneNumberState,
                        sex = UserSex.entries[sexState].name
                    )
                    fireRepository.createUser(user)
                }
                else null
            }

            val result = deferred.await()

            if (result == null)
                _uiState.value = UiState.Failure(null)
            else if (result.isFailure)
                _uiState.value = UiState.Failure(result.exceptionOrNull())
            else
                _uiState.value = UiState.Success(result.getOrDefault(""))
        }
    }
}