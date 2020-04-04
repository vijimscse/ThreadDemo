package com.learn.threaddemo

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity.javaClass.simpleName
    }

    private lateinit var customHandler: CustomHandler
    private lateinit var handlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Thread and its looper implementation
         */
        val looperThread = LooperThread("Custom Looper Thread");
        looperThread.start()

        text.post(Runnable {
            LooperThread2(looperThread.mHandler).start()
        })

        /**
         * HandlerThread Implementation
         */
        handlerThread = HandlerThread("").apply {
            start()
            customHandler = CustomHandler(looper)

        }

        customHandler?.obtainMessage()?.also { msg ->
            msg.what = 101
            customHandler?.sendMessage(msg)
        }
    }

    class LooperThread(name: String) : Thread(name) {
        lateinit var mHandler: Handler
        override fun run() {
            Looper.prepare()
            mHandler = object : Handler() {
                override fun handleMessage(msg: Message?) {
                    Log.d(TAG, "Looper name " + looper.thread.name)
                    Log.d(TAG, "Inside handle message " + msg?.what)
                    looper.thread.interrupt()

                }
            }
            Looper.loop()
        }
    }

    class LooperThread2(private val handler: Handler?) : Thread() {

        override fun run() {
            handler?.sendEmptyMessage(100)
        }
    }

    class CustomHandler(looper: Looper): Handler(looper) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            Log.d(TAG, "Inside handle message " + msg?.what)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        customHandler.looper.quit()
    }
}

