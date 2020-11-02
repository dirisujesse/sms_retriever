package com.roava.sms_retriever_neo

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Telephony
import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry.Registrar
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

class SmsRetrieverNeoPlugin(private val registrar: Registrar) : MethodCallHandler, BroadcastReceiver() {
    var context: Context? = null

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "sms_retriever")
            channel.setMethodCallHandler(SmsRetrieverNeoPlugin(registrar))
        }
    }

    init {
        this.context = registrar.context()
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getAppSignature" -> {
                val signature = AppSignatureHelper(this.context!!).getAppSignatures()[0]
                result.success(signature);
            }
            "startListening" -> {
                startListening()
                this.context!!.registerReceiver(this, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
                result.success(null)
            }
            "stopListening" -> unregister()
            else -> result.notImplemented()
        }
    }


    private fun startListening() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        val client = SmsRetriever.getClient(this.context!! /* context */)

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        val task = client.startSmsRetriever()

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener {
            Log.e(javaClass::getSimpleName.name, "task started")

        }

        task.addOnFailureListener {
            Log.e(javaClass::getSimpleName.name, "task starting failed")
        }

    }

    private fun unregister() {
        this.context!!.unregisterReceiver(this);
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status
            val channel = MethodChannel(registrar.messenger(), "sms_retriever")
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val sms = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    if (sms != null && sms.isNotEmpty()) {
                        channel.invokeMethod("sms_received", sms)
                    }
                    Log.e(javaClass::getSimpleName.name, sms ?: "sms received")
                }

                CommonStatusCodes.TIMEOUT -> {
                    channel.invokeMethod("sms_timeout", null)
                    Log.e(javaClass::getSimpleName.name, "sms timeout")
                }
            }
        }
    }
}

