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
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

/*
Due to the fact that the customer's authentication requirements are quite unusual,
it has become impossible to use the built-in authentication from Firebase.
Therefore, the login method is mail and password,
and the mail is set in the following form: "phone_number@gmail.com".
*/

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val fireRepository: FireRepository,
    private val userData: UserData,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.NotStarted())
    val uiState : StateFlow<UiState> = _uiState

    // user data
    private var firstNameState by mutableStateOf("")
    private var secondNameState by mutableStateOf("")
    private var sexState by mutableStateOf(2)
    var phoneNumberState by mutableStateOf("")
    private var passwordState by mutableStateOf("")

    // sending application for registration, if successful - navigates to code verification
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

    fun signIn(
        phoneNumber: String,
        password : String,
        onSuccess : () -> Unit,
        errorCallback : (String) -> Unit
    ) = viewModelScope.launch {
        try {
            _uiState.value = UiState.Loading()
            launch {
                val firebaseJob = launch(Dispatchers.IO) {
                    auth.signInWithEmailAndPassword("$phoneNumber@gmail.com", password).await()
                }
                firebaseJob.join()
                userData.loadUserDataFromDatabase().join()

                _uiState.value = UiState.Success()
                onSuccess()
            }
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

    fun verifyCode(
        code : String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading()

        val getDeferred = async(Dispatchers.IO) {
            roomRepository.getRegistrationRequest(phoneNumberState)
        }
        val existingRequest = getDeferred.await()
        if (existingRequest?.code?.toString() == code){
            register(onSuccess = onSuccess, onError = onFailure)
        }
        else {
            _uiState.value = UiState.Failure(Exception("Неверный код"))
            onFailure("Неверный код")
        }

    }


    private fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {

        val registrationDeferred = async {

            auth.createUserWithEmailAndPassword(
                "$phoneNumberState@gmail.com", passwordState
            ).await()

            userData.initializeUserData(
                firstName = firstNameState,
                secondName = secondNameState,
                phoneNumber = phoneNumberState,
                sexInd = sexState
            )

            createUserInDatabase()

        }

        val registrationResult = registrationDeferred.await()

        val clearJob = launch(Dispatchers.IO) {
            roomRepository.clearAllRegistrationRequests()
        }
        clearJob.join()

        if (registrationResult == null || registrationResult.isFailure){
            val message = try {
                (_uiState.value.data as Exception).message
            } catch (e : Exception){ "" }
            _uiState.value = UiState.Failure(registrationResult?.exceptionOrNull())
            onError("Ошибка.\n$message")
        }
        else {
            _uiState.value = UiState.Success(registrationResult.getOrDefault(""))
            onSuccess()
        }

    }

    private suspend fun createUserInDatabase() =
        withContext(Dispatchers.IO) {
            val deferred = async {
                val uid = auth.uid
                if (uid != null) {
                    val user = User(
                        id = uid,
                        firstName = firstNameState,
                        secondName = secondNameState,
                        phoneNumber = phoneNumberState,
                        sex = UserSex.entries[sexState].name
                    )
                    fireRepository.createUser(user)
                } else null
            }

            deferred.await()
        }

}