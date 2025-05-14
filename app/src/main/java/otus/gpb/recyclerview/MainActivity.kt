package otus.gpb.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import otus.gpb.recyclerview.data.ChatRepository
import otus.gpb.recyclerview.data.ChatViewModel
import otus.gpb.recyclerview.data.initDB
import otus.gpb.recyclerview.data.loadMoreChatsFromDB
import otus.gpb.recyclerview.ui.ChatItemDecorator
import otus.gpb.recyclerview.ui.ChatItemTouchHelper
import otus.gpb.recyclerview.ui.ChatListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatListAdapter
    private lateinit var recyclerView: RecyclerView
    private val repository = ChatRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDB()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatListAdapter()

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel(repository) as T
            }
        })[ChatViewModel::class.java]

        lifecycleScope.launch {
            viewModel.chats.collect { chats ->
                adapter.submitList(chats)
            }
        }

        recyclerView.addItemDecoration(ChatItemDecorator(this))
        recyclerView.apply {
            clipChildren = false
            clipToPadding = false
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleChat = layoutManager.findLastVisibleItemPosition()
                val chatsCount = layoutManager.itemCount

                if (!viewModel.isLoading.value && lastVisibleChat >= chatsCount - 5) {
                    viewModel.loadMoreChats()
                }
            }
        })

        recyclerView.adapter = adapter
        ItemTouchHelper(ChatItemTouchHelper(viewModel)).attachToRecyclerView(recyclerView)

    }
}