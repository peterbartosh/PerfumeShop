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
    //val loadingState = MutableStateFlow(LoadingState.IDLE)


    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _auth = FirebaseAuth.getInstance()
    var storedVerificationId: String = ""
    private var firstAndSecondName by mutableStateOf("")
    private var sexP by mutableStateOf(2)

    fun clear() {
        super.onCleared()
    }

    fun onLoginClicked(
        displayName : String,
        sexInd : Int,
        phoneNumber: String,
        onCodeSent: () -> Unit,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {

        _auth.setLanguageCode("ru")

        firstAndSecondName = displayName
        sexP = sexInd

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
                Log.d("phoneBook", "code sent $verificationId")
                storedVerificationId = verificationId
                onCodeSent()
            }
        }

        Log.d("PHONE_TEST", "onLoginClicked: $phoneNumber")

        val options =
            PhoneAuthOptions.newBuilder(_auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .build()

        if (options != null) {
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }


    fun verifyPhoneNumberWithCode(code: String, onSuccess: () -> Unit) = viewModelScope.launch {
        Log.d("PHONE_TEST", "verifyPhoneNumberWithCode: $storedVerificationId")
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential, onSuccess)
    }


    private suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {


            _auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("DISP_NAME_TEST", FirebaseAuth.getInstance().currentUser?.displayName.toString())

                        if (firstAndSecondName.trim().isNotEmpty())
                            _auth.currentUser?.updateProfile(
                                UserProfileChangeRequest.Builder()
                                    .setDisplayName("$firstAndSecondName|$sexP").build()
                        )
//                    if (_auth.currentUser?.phoneNumber?.isNullOrEmpty() == true)
//                        _auth.currentUser?.updatePhoneNumber(credential.)
                        onSuccess()
                        Log.d("DISP_NAME_TEST", FirebaseAuth.getInstance().currentUser?.displayName.toString())

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







