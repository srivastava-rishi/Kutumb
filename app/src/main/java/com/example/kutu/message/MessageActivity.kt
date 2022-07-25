package com.example.kutu.message

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutu.R
import com.example.kutu.base.BaseActivity
import com.example.kutu.data.db.database.MessageDatabase
import com.example.kutu.databinding.ActivityMessageBinding
import com.example.kutu.message.adapter.MessageAdapter
import com.example.kutu.util.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessageActivity : BaseActivity() {

    lateinit var binding: ActivityMessageBinding

    private lateinit var messageAdapter: MessageAdapter

    @Inject
    lateinit var messageDatabase: MessageDatabase

    val TAG = "@MessageActivity"

    var addressId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)

        initTheme()
        initData()

    }

    private fun initData() {

        var colorCode = intent.getIntExtra(Constant.COLOR_CODE, 8)
        addressId = intent.getStringExtra(Constant.ADDRESS_ID)
        binding.tvPersonName.text = addressId
        initRecyclerView(colorCode)

        loadMessages(addressId!!)

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun loadMessages(addressId: String) {

        val lastMessageDao = messageDatabase.messageDao()

        CoroutineScope(Dispatchers.IO).launch {
            val data = lastMessageDao.getAllMessages(addressId)
            Log.d(TAG, "loadMessages: $data")

            try {
                runOnUiThread {
                    messageAdapter.submitList(data)
                }
            } catch (e: Exception) {
                Log.d(TAG, "loadMessages: " + "line no 100")
            }

        }

    }

    private fun initRecyclerView(colorCode: Int) {

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        llm.stackFromEnd = true
        binding.rvRecyclerView.setHasFixedSize(true)
        binding.rvRecyclerView.layoutManager = llm
        messageAdapter = MessageAdapter(this, colorCode)
        binding.rvRecyclerView.adapter = messageAdapter
    }

    private fun initTheme() {
        window.statusBarColor = resources.getColor(R.color.statusBar)
        window.navigationBarColor = resources.getColor(R.color.white2)
    }


}