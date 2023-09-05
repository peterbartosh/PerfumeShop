package com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    //val loadingState = MutableStateFlow(LoadingState.IDLE)



    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _auth = FirebaseAuth.getInstance()
    var storedVerificationId : String = ""

    fun clear(){
        super.onCleared()
    }

    fun onLoginClicked(phoneNumber: String,
                       onCodeSent: () -> Unit,
                       onSuccess: () -> Unit)  = viewModelScope.launch {

        _auth.setLanguageCode("ru")

        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("phoneBook", "verification completed")
                viewModelScope.launch {
                    signInWithPhoneAuthCredential(credential, onSuccess)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("phoneBook", "verification failed" + p0)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("phoneBook", "code sent" + verificationId)
                storedVerificationId = verificationId
                onCodeSent()
            }

        }
        val options =
            PhoneAuthOptions.newBuilder(_auth)
                .setPhoneNumber("+375$phoneNumber")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .build()

        if (options != null) {
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }


     fun verifyPhoneNumberWithCode(code: String, onSuccess : () -> Unit)  = viewModelScope.launch {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential, onSuccess)
    }



    private suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,
                                              onSuccess : () -> Unit) {

        _auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    onSuccess.invoke()
                    Log.d("phoneBook", "logged in")
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.d("phoneBook", "wrong otp")
                    }
                    // Update UI
                }
            }
    }




    }








