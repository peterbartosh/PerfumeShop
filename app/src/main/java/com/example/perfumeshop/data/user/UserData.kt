package com.example.perfumeshop.data.user

import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.utils.UserSex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object UserData {

    private val _usersCollection = FirebaseFirestore.getInstance().collection("users")

    private var _user : User? = null
    var user : User? = _user

    fun initializeUserData(
        firstName : String? = null,
        secondName : String? = null,
        phoneNumber : String? = null,
        sexInd : Int = 2,
        streetName : String? = null,
        homeNumber : String? = null,
        flatNumber : String? = null
    ){
        val uid = FirebaseAuth.getInstance().uid

        if (uid != null) {
            _user = User(
                id = uid,
                firstName = firstName,
                secondName = secondName,
                phoneNumber = phoneNumber,
                sex = UserSex.entries[sexInd].name,
                street = streetName,
                home = homeNumber,
                flat = flatNumber
            )
            user = _user
        }
    }

    fun loadUserData(){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.uid
        if (uid != null) {
            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
                CoroutineScope(Job()).launch {
                    _user = _usersCollection.document(uid)
                        .get().await().toObject(User::class.java)
                    user = _user
                }
        }
    }

    fun updateUserData(
        firstName : String? = null,
        secondName : String? = null,
        sexInd : Int = 2,
        streetName : String? = null,
        homeNumber : String? = null,
        flatNumber : String? = null
    ){
        _user?.firstName = firstName
        _user?.secondName = secondName
        _user?.sex = UserSex.entries[sexInd].name
        _user?.street = streetName
        _user?.home = homeNumber
        _user?.flat = flatNumber

        val uid = _user?.id
        if (uid != null)
            CoroutineScope(Job()).launch {
                _usersCollection.document(uid).set(_user ?: User()).await()
                user = _user
            }
    }
}