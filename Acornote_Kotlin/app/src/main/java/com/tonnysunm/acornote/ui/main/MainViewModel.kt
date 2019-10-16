package tonnysunm.com.acornote.ui.main

import androidx.lifecycle.*
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

}
