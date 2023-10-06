package com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui



import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.mail.EmailSender
import com.example.perfumeshop.data_layer.models.User
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.data_layer.utils.Sex
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {


//    private val _loading = MutableLiveData(false)
//    val loading: LiveData<Boolean> = _loading

    var isLoading by mutableStateOf(false)

    private val _auth = FirebaseAuth.getInstance()

    private var verCode : String? = null

    private var firstNameState by mutableStateOf("")
    private var secondNameState by mutableStateOf("")
    private var sexState by mutableStateOf(2)
    private var phoneNumberState by mutableStateOf("")
    private var passwordState by mutableStateOf("")

    fun notifyAdmin(
        firstName : String, secondName : String,
        phoneNumber : String, sexInd : Int,
        pwd : String, seed : Int,
        onAdminNotified : () -> Unit
    ) = viewModelScope.launch {
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

    fun register(){
        _auth.createUserWithEmailAndPassword(
            "$phoneNumberState@gmail.com", passwordState
        ).addOnSuccessListener {
            Log.d("AUTH_TEST", "createUser: ${_auth.currentUser?.email}")
            _auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(
                    "$firstNameState,$secondNameState,$sexState,$phoneNumberState,,,"
                ).build()
            )
            createUserInDatabase()
        }
    }

    fun signIn(phoneNumber: String, password : String){
        Log.d("AUTHH_TEST", "signIn: $phoneNumber, $password")
        _auth.signInWithEmailAndPassword("$phoneNumber@gmail.com", password)
    }

    private fun createUserInDatabase() {
        val id = _auth.uid
        val sexes = listOf(Sex.Male, Sex.Female, Sex.Unisex)
        if (id != null) {
           val user = User(
                id = id,
                firstName = firstNameState,
                secondName = secondNameState,
                phoneNumber = phoneNumberState,
                sex = sexes[sexState].name
            )
            repository.createUser(user)
        }
    }














    var storedVerificationId: String = ""




    fun clear() {
        super.onCleared()
    }

    fun onLoginClicked(
        firstName : String,
        secondName : String,
        sexInd : Int,
        phoneNumber: String,
        onCodeSent: () -> Unit,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {

        isLoading = true

        _auth.setLanguageCode("ru")

        firstNameState = firstName
        secondNameState = secondName
        sexState = sexInd

        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("PHONE_AUTH_TEST", "verification completed")
                viewModelScope.launch {
                    signInWithPhoneAuthCredential(credential, onSuccess)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("PHONE_AUTH_TEST", "verification failed" + p0)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("PHONE_AUTH_TEST", "code sent $verificationId")
                storedVerificationId = verificationId
                onCodeSent()
                isLoading = false
            }
        }

        val options =
            PhoneAuthOptions.newBuilder(_auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .build()

        if (options != null) {
            Log.d("PHONE_AUTH_TEST", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }


    fun verifyPhoneNumberWithCode(code: String, onSuccess: () -> Unit) = viewModelScope.launch {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential, onSuccess)
    }


    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {
            _auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        if (firstNameState.isNotEmpty() && secondNameState.isNotEmpty())
                            _auth.currentUser?.updateProfile(
                                UserProfileChangeRequest.Builder()
                                    .setDisplayName("$firstNameState,$secondNameState,$sexState,,,").build())

//                    if (_auth.currentUser?.phoneNumber?.isNullOrEmpty() == true)
//                        _auth.currentUser?.updatePhoneNumber(credential.)
                        onSuccess()

                    } else Log.d(
                        "ERROR_ERROR",
                        "signInWithPhoneAuthCredential: ${task.exception?.message}"
                    )
                }
        }
    }

}