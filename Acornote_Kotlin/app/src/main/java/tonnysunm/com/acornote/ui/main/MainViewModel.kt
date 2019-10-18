package tonnysunm.com.acornote.ui.main

import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import tonnysunm.com.acornote.model.Item
import java.util.*

class MainViewModel : ViewModel() {
    //
    val userId: String = ""

    //liveData
    private val _data: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>().also {
            loadData()
        }
    }
    var data: LiveData<List<Item>> = _data

    var anotherData: LiveData<String> = data.map { "UserId: ${it.first().name}" }

    //async
    fun loadData() {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                //api load data
                delay(2000)

                Date().time.toString()
            }

            _data.value = listOf(Item(data))
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()

    }

    fun onClick(view: View) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
        view.findNavController().navigate(action)
    }

}
