package com.example.perfumeshop.ui_layer.features.main.cart_feature.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.data_layer.utils.updateFieldInDatabase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {
    private var _userProducts = MutableStateFlow<MutableList<Product>>(value = mutableListOf())
    var userProducts: StateFlow<List<Product>> = _userProducts


    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)
    var isInitialized by mutableStateOf(false)

    var initLoadingProducts by mutableStateOf(true)

    init {
        loadUserProducts()
    }

    fun addToCart(product: Product) = viewModelScope.launch{
        _userProducts.value.add(product)
        userProducts = _userProducts
        updateCartInDatabase()
    }

    fun removeFromCart(productId : String) = viewModelScope.launch{
        _userProducts.value.removeIf { p -> p.id == productId }
        userProducts = _userProducts
        updateCartInDatabase()
    }

    fun isInCart(productId : String) : Boolean {
        var answer : Boolean = false
        viewModelScope.launch {
            answer = userProducts.value.find { product -> product.id!! == productId } != null
        }
        return answer
    }

    private fun updateCartInDatabase() = viewModelScope.launch {
        val id = FirebaseAuth.getInstance().uid
        if (!id.isNullOrEmpty())
            updateFieldInDatabase(
                collectionPath = "users",
                id = id,
                fieldPath = "cart",
                updatedValue = _userProducts.value.map { it.id }
            )
    }

    private fun loadUserProducts() {
        val auth = FirebaseAuth.getInstance()
        val id = auth.uid

        initLoadingProducts = false
        isInitialized = true

        if (auth.currentUser?.isAnonymous != true && !id.isNullOrEmpty())
            viewModelScope.launch {
                isFailure = false
                isLoading = true

                repository.getUserCartProducts(id).catch { e ->
                    Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                    isFailure = true
                }.collect { product ->
                    _userProducts.value.add(product)
                }

                userProducts = _userProducts

                isLoading = false

                if (_userProducts.value.isEmpty())
                    Log.d("ERROR_ERROR", "searchQuery: EMPTY")

                if (_userProducts.value.isEmpty() || isFailure)
                    isFailure = true
                else
                    isSuccess = true

            }
    }


//    init {
//        loadUserProducts("MCFFRhmOrGUdZKKExpZS")
//    }


//    fun sendEmail() = viewModelScope.launch {
//            main(
//                arrayOf(
//                    "myEmail@gmail.com",
//                    "password1",
//                    "myEmail@gmail.com",
//                    "myEmail@gmail.com",
//                    "myEmail@gmail.com"
//                )
//            )
//    }
//
//            private fun main(args: Array<String>) {
//                val userName =  args[0]
//                val password =  args[1]
//
//                val emailFrom = args[2]
//                val emailTo = args[3]
//                val emailCC = args[4]
//
//                val subject = "SMTP Test"
//                val text = "Hello Kotlin Mail"
//
//                val props = Properties()
//                putIfMissing(props, "mail.smtp.host", "smtp.office365.com")
//                putIfMissing(props, "mail.smtp.port", "587")
//                putIfMissing(props, "mail.smtp.auth", "true")
//                putIfMissing(props, "mail.smtp.starttls.enable", "true")
//
//                val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
//                    override fun getPasswordAuthentication(): PasswordAuthentication {
//                        return PasswordAuthentication(userName, password)
//                    }
//                })
//
//                session.debug = true
//
//                try {
//                    val mimeMessage = MimeMessage(session)
//                    mimeMessage.setFrom(InternetAddress(emailFrom))
//                    mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false))
//                    mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailCC, false))
//                    mimeMessage.setText(text)
//                    mimeMessage.subject = subject
//                    mimeMessage.sentDate = Date()
//
//                    val smtpTransport = session.getTransport("smtp")
//                    smtpTransport.connect()
//                    smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
//                    smtpTransport.close()
//                } catch (messagingException: MessagingException) {
//                    messagingException.printStackTrace()
//                }
//            }
//
//            private fun putIfMissing(props: Properties, key: String, value: String) {
//                if (!props.containsKey(key)) {
//                    props[key] = value
//                }
//            }
//
//        }
//    }

//    fun sendEmail() = viewModelScope.launch {
//        val email = emailBuilder {
//            from("foo@bar.com")
//            to("info@example.org")
//
//            withSubject("Important question")
//            withPlainText("Hey, how are you today?")
//
//            // and much more
//        }
//
//        kotlin.runCatching { email.send().join() }
//            .onFailure { it.printStackTrace() }
//            .onSuccess { println("I just sent an email!") }
//
//    }


}