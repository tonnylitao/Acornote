package tonnysunm.com.acornote.ui.note.list

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.extensions.getColorString
import tonnysunm.com.acornote.model.ColorTag
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.model.Repository


class DetailViewModelFactory(private val application: Application, private val filter: NoteFilter) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        NoteListViewModel(application, filter) as T

}

class NoteListViewModel(application: Application, filter: NoteFilter) :
    AndroidViewModel(application) {

    val repository: Repository by lazy { Repository(application) }

    val noteFilterLiveData: MutableLiveData<NoteFilter> by lazy {
        MutableLiveData(filter)
    }

//    val createVisibilityLiveData: LiveData<Int> = Transformations.map(noteFilterLiveData) {
//        return@map when (it) {
//            NoteFilter.All, is NoteFilter.ByLabel -> View.VISIBLE
//            else -> View.GONE
//        }
//    }

    val data: LiveData<PagedList<Note>> = noteFilterLiveData.switchMap {
        repository.notes(it).toLiveData(pageSize = 5)
    }

    suspend fun updateNotes(list: MutableList<Note>) = repository.noteDao.updateNotes(list)

    fun createColorTagsIfNecessary(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.colorTagDao.notesCount()
            if (count == 0) {
                val initialData = listOf(
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_black),
                        name = "Black"
                    ),
                    ColorTag(color = context.getColorString(R.color.color_tag_gray), name = "Gray"),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_green),
                        name = "Green"
                    ),
                    ColorTag(color = context.getColorString(R.color.color_tag_blue), name = "Blue"),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_orange),
                        name = "Orange"
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_purple),
                        name = "Purple"
                    ),
                    ColorTag(color = context.getColorString(R.color.color_tag_red), name = "Red"),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_yellow),
                        name = "Yellow"
                    )
                ).reversed()
                repository.colorTagDao.insert(initialData)
            }
        }
    }
}
