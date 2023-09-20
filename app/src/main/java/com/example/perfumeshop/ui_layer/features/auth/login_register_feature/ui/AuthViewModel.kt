package com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui



import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {


//    private val _loading = MutableLiveData(false)
//    val loading: LiveData<Boolean> = _loading

    var isLoading by mutableStateOf(false)

    private val _auth = FirebaseAuth.getInstance()
    var storedVerificationId: String = ""


    private var firstNameState by mutableStateOf("")
    private var secondNameState by mutableStateOf("")
    private var sexState by mutableStateOf(2)

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

    private suspend fun createUser(user: User) {
        val id = _auth.uid
        if (id != null)
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(id)
                .set(user)
                .addOnSuccessListener { Log.d("AUTH_CREATE_USER", "createUser: SUCCESS") }
                .addOnFailureListener { Log.d("AUTH_CREATE_USER", "createUser: FAILED") }
    }
}







