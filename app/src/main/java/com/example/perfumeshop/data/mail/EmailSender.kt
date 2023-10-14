package com.example.perfumeshop.data.mail


import android.util.Log
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.models.Order
import com.example.perfumeshop.data.models.ProductWithAmount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class EmailSender() {

    fun s() {

    }

    fun sendRegisterRequestEmail(fN: String, sN: String, pN: String, code: Int) {

        val stringSenderEmail = "goldappsender@gmail.com"
        val stringReceiverEmail = "pt.bartosh54321@gmail.com"
        val stringPasswordSenderEmail = "tccw syra ghsy reib"
        val stringHost = "smtp.gmail.com"
        val properties = System.getProperties()

        properties["mail.smtp.host"] = stringHost
        properties["mail.smtp.port"] = "465"
        properties["mail.smtp.ssl.enable"] = "true"
        properties["mail.smtp.auth"] = "true"
        properties["mail.debug"] = "true"

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
            }
        })

        val mimeMessage = MimeMessage(session)

        mimeMessage.addRecipient(
            Message.RecipientType.TO,
            InternetAddress(stringReceiverEmail)
        )

        mimeMessage.subject = "Заявка на регистрацию."

        mimeMessage.setContent(
//            "<h2> Подтвердить регистрацию.\n </h2>" +
            "<h3>Имя: $sN $fN</h3>" +
                    "<h3>Номер телефона: $pN</h3>" +
                    "<h3>Код: $code</h3>",
            "text/html; charset=utf-8"
        )


        CoroutineScope(Job() + Dispatchers.Default).launch {

            try {
                Transport.send(mimeMessage)
            } catch (e: Exception) {
                Log.d("ERROR_ERROR", e.message.toString())
            }

        }


        // create mime message
    }

    suspend fun sendOrderEmail(
        order: Order,
        products: List<ProductWithAmount>,
        scope: CoroutineScope
    ): Pair<Boolean, Exception?> = scope.async {

            val stringSenderEmail = "goldappsender@gmail.com"
            val stringReceiverEmail = "pt.bartosh54321@gmail.com"
            val stringPasswordSenderEmail = "tccw syra ghsy reib"
            val stringHost = "smtp.gmail.com"
            val properties = System.getProperties()

            properties["mail.smtp.host"] = stringHost
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"
            properties["mail.debug"] = "true"

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
                }
            })

            val mimeMessage = MimeMessage(session)
            mimeMessage.addRecipient(
                Message.RecipientType.TO,
                InternetAddress(stringReceiverEmail)
            )

            mimeMessage.subject = "Заказ-${order.number}"

            val sep = ","

            val values = mutableListOf(
                order.number, order.address, UserData.user?.firstName,
                UserData.user?.secondName,
                UserData.user?.phoneNumber
            )

            val orderDesc = StringBuilder("")

            var state: Pair<Boolean, Exception?> = Pair(false, null)

            if (values.any { it?.isEmpty() == true })
                delay(200)
            else
                orderDesc.append(
                    "Номер заказа: ${order.number}\n" +
                            "Адрес дооставки: ${order.address}\n" +
                            "Клиент: ${
                                UserData.user?.secondName + " " +
                                        UserData.user?.firstName
                            }\n" +
                            "Номер телефона: ${UserData.user?.phoneNumber}\n"
                )


            for (i in 0..5) {
                if (products.isEmpty()) delay(200)
                else break
            }

            products.forEachIndexed{ ind, productWithAmount ->
                orderDesc.append(
                    "  ${ind + 1}) Идентификатор товара: ${productWithAmount.product?.id}\n" +
                            "      Брэнд: ${productWithAmount.product?.brand}\n" +
                            "      Количество: ${productWithAmount.amount}\n"
                )
            }

            mimeMessage.setText(orderDesc.toString())


            state = try {
                Transport.send(mimeMessage)
                Pair(true, null)
            } catch (e: MessagingException) {
                Log.d("ERROR_ERROR_1", e.message ?: "Unknown error message")
                Pair(false, e)
            }

            state

        }.await()
}

