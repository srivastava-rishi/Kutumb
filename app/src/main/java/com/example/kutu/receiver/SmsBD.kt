package com.example.kutu.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.*
import android.telephony.SmsMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kutu.R
import com.example.kutu.util.AppUtil


class SmsBD : BroadcastReceiver() {

    private val smsReceived = "android.provider.Telephony.SMS_RECEIVED"
     var logTag = "@SmsBroadcastReceiver"

    // declaring variables
    private var notificationId = 302
    var channelId = "channelId"

    override fun onReceive(p0: Context, intent: Intent) {


        if (intent.action.equals(smsReceived)) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<*>?
                val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus!!.size)
                for (i in pdus.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }
                if (messages.size > -1) {
                    val message = messages[0]!!.messageBody
                    val address = messages[0]!!.originatingAddress

                    // text
                    var answerList = AppUtil.getAnswerList(message)

                    if (answerList[2] == 0){
                        createNotification(p0, message,address!! )
                        sendNotification(p0,message, address)

                    }
                }

            }
        }

    }

    private fun createNotification(context: Context,message: String, address:String) {

        if (VERSION.SDK_INT >= VERSION_CODES.O) {

            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel =
                NotificationChannel(channelId, address, importance).apply {
                    description = message
                }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(context: Context,message: String, address:String){

        val builder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.message)
            .setContentTitle(address)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)){
            notify(notificationId,builder.build())
        }

    }
}