package com.example.perfumeshop.presentation.features.auth.login_register_feature.ui



import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.utils.UserSex
import com.example.perfumeshop.presentation.app.ui.ERROR_TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    var isLoading by mutableStateOf(false)

    private val _auth = FirebaseAuth.getInstance()

    private var verCode : String? = null

    private var firstNameState by mutableStateOf("")
    private var secondNameState by mutableStateOf("")
    private var sexState by mutableStateOf(2)
    private var phoneNumberState by mutableStateOf("")
    private var passwordState by mutableStateOf("")

    private fun checkBlackList(phoneNumber: String) = viewModelScope.launch {
        repository.isInBlackList(phoneNumber)
    }

    fun notifyAdmin(
        firstName : String, secondName : String,
        phoneNumber : String, sexInd : Int,
        pwd : String, seed : Int,
        isInBlackList : () -> Unit,
        onAdminNotified : () -> Unit
    ) = viewModelScope.launch {

        if (repository.isInBlackList(phoneNumber)) {
            isInBlackList()
            return@launch
        }

        firstNameState = firstName
        secondNameState = secondName
        sexState = sexInd
        phoneNumberState = phoneNumber
        passwordState = pwd

        val code = Random(seed).nextInt(100,999)
        verCode = code.toString()
        val emailSender = EmailSender()
        emailSender.sendRegisterRequestEmail(firstName, secondName, phoneNumber, code)

        onAdminNotified()
    }

    fun verifyCode(code : String) : Boolean {
        return code == verCode
    }

    fun register() = viewModelScope.launch {
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
    }

    fun signIn(
        phoneNumber: String,
        password : String,
        onSuccess : () -> Unit,
        errorCallback : (String) -> Unit
    ) = viewModelScope.launch {
        try {
            _auth.signInWithEmailAndPassword("$phoneNumber@gmail.com", password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        UserData.loadUserData()
                        onSuccess()
                    } else {
                        Log.d(ERROR_TAG, "signIn: ${it.exception?.message}")
                        errorCallback(it.exception?.message.toString())
                    }

                }.await()
        } catch (e : FirebaseAuthInvalidCredentialsException){
            Log.d(ERROR_TAG, "signIn: invalid credentials")
            errorCallback("Ошибка.\nПользователь не найден.")
        } catch (e : Exception){
            Log.d(ERROR_TAG, "signIn: ${e.message}")
            errorCallback(e.message.toString())
        }
    }

    private fun createUserInDatabase() = viewModelScope.launch {
        val uid = _auth.uid
        if (uid != null) {
           val user = User(
                id = uid,
                firstName = firstNameState,
                secondName = secondNameState,
                phoneNumber = phoneNumberState,
                sex = UserSex.entries[sexState].name
            )
            repository.createUser(user).let { result ->
                //
            }
        }
    }
}