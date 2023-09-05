package com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui

//
//@SuppressLint("SuspiciousIndentation")
//fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit)
//        = viewModelScope.launch{
//    _auth.signInWithEmailAndPassword(email, password)
//        .addOnCompleteListener{ task ->
//            if (task.isSuccessful) {
//                //Log.d("SIGN_IN_SUC", "signIn: ")
//                home()
//            } else
//                Log.d("SIGN_IN_ERROR", "signIn: ${task.exception}")
//        }
//
//}
//
//
//fun createAccount(email: String,
//                  password: String,
//                  firstName : String,
//                  secondName : String,
//                  phoneNumber : String,
//                  sex : String,
//                  country : String,
//                  home: () -> Unit){
//    if (_loading.value == false){
//        _loading.value = true
//
//
//
////            val actionCodeSettings = actionCodeSettings {
////                // URL you want to redirect back to. The domain (www.example.com) for this
////                // URL must be whitelisted in the Firebase Console.
////                url = "someUrl"                                         //todo
////                // This must be true
////                handleCodeInApp = true
////                //setIOSBundleId("com.example.ios")
////                setAndroidPackageName(
////                    "com.example.android",
////                    true, // installIfNotAvailable
////                    "12", // minimumVersion
////                )
////            }
////
////            _auth.sendSignInLinkToEmail(email, actionCodeSettings)
////                .addOnCompleteListener{task ->
////                   // if (task.isSuccessful)
////                    val u = FirebaseAuth.getInstance().currentUser
////                    u?.isEmailVerified
////                }
//
//
//
//        _auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener{ task ->
//
////                    createUser(firstName = firstName, secondName = secondName,
////                               phoneNumber = phoneNumber, sex = sex, country = country, email = email)
//
//
//                if (task.isSuccessful){
//
//                    createUser(firstName = firstName, secondName = secondName,
//                               phoneNumber = phoneNumber, sex = sex, country = country, email = email)
//
//
//                    //home()
//                } else {
//                    Log.d("ERROR_ERROR",
//                          "createAccount: ${task.exception?.message}")
//                }
//            }
//    }
//}
//
//private fun createUser(firstName : String,
//                       secondName : String,
//                       phoneNumber : String,
//                       email: String,
//                       sex : String,
//                       country : String,) {
//
//    val userID = _auth.currentUser?.uid
//
//
//
//    val user = User(userAuthId = userID.toString(), firstName = firstName, secondName = secondName,
//                    phoneNumber = phoneNumber, sex = sex, country = country, email = email, products = listOf()
//    )
//
//    // val user = mutableMapOf<String, Any>("user_id" to userID.toString(), "name" to "James")
//
//    saveToFirebase(item = user, collectionName = "users")
//}
//
//
