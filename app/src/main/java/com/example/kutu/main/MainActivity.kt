package com.example.kutu.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutu.R
import com.example.kutu.base.BaseActivity
import com.example.kutu.data.db.database.AddressDatabase
import com.example.kutu.data.db.database.LastMessageDatabase
import com.example.kutu.data.db.database.MessageDatabase
import com.example.kutu.data.db.entity.AddressEntity
import com.example.kutu.data.db.entity.LastMessageEntity
import com.example.kutu.data.db.entity.MessageEntity
import com.example.kutu.databinding.ActivityMainBinding
import com.example.kutu.main.adapter.MainAdapter
import com.example.kutu.message.MessageActivity
import com.example.kutu.util.AppUtil
import com.example.kutu.util.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() , MainAdapter.MainAdapterListener{

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var addressDatabase: AddressDatabase

    @Inject
    lateinit var lastMessageDatabase: LastMessageDatabase

    @Inject
    lateinit var messageDatabase: MessageDatabase

    private lateinit var mainAdapter: MainAdapter

    private var TAG = "@MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initTheme()
        //
        binding.tvNoMessage.visibility = View.VISIBLE
        binding.llContainer.visibility = View.VISIBLE

        initRecyclerView()

        binding.pbSyncing.visibility = View.VISIBLE

        dbOperation()

    }

    private fun  initRecyclerView(){

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL

        binding.rvRecyclerView.setHasFixedSize(true)
        binding.rvRecyclerView.layoutManager = llm
        mainAdapter = MainAdapter(this,this)
        binding.rvRecyclerView.adapter = mainAdapter
    }

    private fun loadMessages() {

        val lastMessageDao = lastMessageDatabase.lastMessageDao()

        CoroutineScope(Dispatchers.IO).launch {
            val data = lastMessageDao.getAllLastMessages()

            try {
                runOnUiThread{
                    mainAdapter.submitList(data)
                }
            }catch (e:Exception){
            }

        }

    }

    private fun dbOperation() {

        val addressDao2 = addressDatabase.addressDao()

        CoroutineScope(Dispatchers.IO).launch {
            val data = addressDao2.getAllAddress()
            doSomeThing(data)
        }

    }

    private fun doSomeThing(data: List<AddressEntity>){


        for (i in data.indices) {

            val hh: String = data[i].addressId

            getAllMessageFromASingleNumber(hh)
        }

        loadMessages()

        try {
            runOnUiThread{
                binding.rvRecyclerView.visibility = View.VISIBLE
                binding.tvNoMessage.visibility = View.GONE
                binding.llContainer.visibility = View.GONE
            }

        }catch (e:Exception){
            Log.d(TAG, "doSomeThing: " + "line no 207")
        }



    }

    @SuppressLint("Range")
    fun getAllMessageFromASingleNumber(selection: String) {

        var temp: Int = 0;

        val uriSmsUriInbox = Uri.parse("content://sms/inbox")
        val uriSmsUriSent = Uri.parse("content://sms/sent")
        val projection = arrayOf("_id", "thread_id", "address", "body", "date", "type")


        val curI: Cursor? = contentResolver.query(uriSmsUriInbox, projection, "address = '$selection'", null, null)
        val curS: Cursor? = contentResolver.query(uriSmsUriSent, projection, "address = '$selection'", null, null)


        while (curI!!.moveToNext()) {

            val id: String = curI.getString(curI.getColumnIndex("_id"))
            val threadId: String = curI.getString(curI.getColumnIndex("thread_id"))
            val address: String =  curI.getString(curI.getColumnIndexOrThrow("address")).toString()
            val body: String = curI.getString(curI.getColumnIndexOrThrow("body"))
            val date: Long =  curI.getLong(curI.getColumnIndexOrThrow("date"))
            val type: Int =  curI.getInt(curI.getColumnIndexOrThrow("type"))

            if (temp == 0) {

                var colorCode:Int = AppUtil.getViewCount()
                Log.d(TAG, "getAllMessageFromASingleNumber: colorCode $colorCode")

                 val lastMessageEntity = LastMessageEntity(
                        addressId = address,
                        lastMessage = body,
                        imageColor = colorCode,
                        timeStamp = date,
                        type = type
                    )

                    val lastMessage = lastMessageDatabase.lastMessageDao()

                    CoroutineScope(Dispatchers.Main).launch {
                        lastMessage.insertLastMessageAddress(lastMessageEntity)
                    }


                temp++

            }
            val messageEntity = MessageEntity(
                messageId = id,
                addressId = address,
                message = body,
                timestamp = date,
                type = type
            )

            val message = messageDatabase.messageDao()

            CoroutineScope(Dispatchers.IO).launch {
                message.insertMessage(messageEntity)
            }
        }

        while (curS!!.moveToNext()) {

            val id: String = curS.getString(curS.getColumnIndex("_id"))
            val threadId: String = curS.getString(curS.getColumnIndex("thread_id"))
            val address: String =  curS.getString(curS.getColumnIndexOrThrow("address")).toString()
            val body: String = curS.getString(curS.getColumnIndexOrThrow("body"))
            val date: Long =  curS.getLong(curS.getColumnIndexOrThrow("date"))
            val type: Int =  curS.getInt(curS.getColumnIndexOrThrow("type"))


            val messageEntity = MessageEntity(
                messageId = id,
                addressId = address,
                message = body,
                timestamp = date,
                type = type
            )

            val message = messageDatabase.messageDao()

            CoroutineScope(Dispatchers.IO).launch {
                message.insertMessage(messageEntity)
            }
        }
        val messageDao = messageDatabase.messageDao()

        CoroutineScope(Dispatchers.IO).launch {
            val data = messageDao.getAllMessages(selection)
            Log.d(TAG, "getAllMessage: $data")
        }


    }


    private fun initTheme(){
        window.statusBarColor = resources.getColor(R.color.statusBar)
        window.navigationBarColor = resources.getColor(R.color.white2)
    }

    override fun onOpenAllMessages(lastMessageEntity: LastMessageEntity) {
        val intent = Intent(this, MessageActivity::class.java)
        intent.putExtra(Constant.ADDRESS_ID, lastMessageEntity.addressId)
        intent.putExtra(Constant.COLOR_CODE, lastMessageEntity.imageColor)
        startActivity(intent)
    }
}