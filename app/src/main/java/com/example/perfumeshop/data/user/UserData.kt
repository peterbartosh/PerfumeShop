package com.example.perfumeshop.data.user

import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.utils.UserSex
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserData @Inject constructor(
    private val roomRepository: RoomRepository,
    private val fireRepository: FireRepository,
    private val auth: FirebaseAuth
) {

    private var _user: User? = null
    var user: User? = _user

    suspend fun initializeUserData(
        firstName: String? = null,
        secondName: String? = null,
        phoneNumber: String? = null,
        sexInd: Int = 2,
        streetName: String? = null,
        homeNumber: String? = null,
        flatNumber: String? = null
    ) = withContext(Dispatchers.IO) {

        auth.uid?.let { uid ->
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
            _user?.let { u ->
                roomRepository.initializeUserData(u)
                fireRepository.updateUserData(u)
            }
            user = _user
        }
    }

    fun loadUserDataFromDatabase() =
        CoroutineScope(Job() + Dispatchers.IO).launch {
            auth.uid?.let { uid ->
                if (auth.currentUser?.isAnonymous == false) {
                    _user = fireRepository.getUserData(uid)
                    _user?.let { u ->
                        roomRepository.initializeUserData(u)
                    }
                }
                user = _user
            }
        }

    fun updateUserData(
        firstName: String? = null,
        secondName: String? = null,
        sexInd: Int = 2,
        streetName: String? = null,
        homeNumber: String? = null,
        flatNumber: String? = null
    ) {

        CoroutineScope(Job() + Dispatchers.IO).launch {
            _user?.firstName = firstName
            _user?.secondName = secondName
            _user?.sex = UserSex.entries[sexInd].name
            _user?.street = streetName
            _user?.home = homeNumber
            _user?.flat = flatNumber

            _user?.let { u ->
                roomRepository.updateUserData(u)
                fireRepository.updateUserData(u)
            }
            user = _user
        }
    }

    fun clearUserData() =
        CoroutineScope(Job() + Dispatchers.IO).launch {
            roomRepository.clearUserData()
            _user = null
            user = null // danger
        }
}