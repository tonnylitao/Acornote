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
                        name = context.getString(R.string.color_tag_black)
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_gray),
                        name = context.getString(R.string.color_tag_gray)
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_green),
                        name = context.getString(R.string.color_tag_green)
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_blue),
                        name = context.getString(R.string.color_tag_blue)
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_orange),
                        name = context.getString(R.string.color_tag_orange)
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_purple),
                        name = context.getString(R.string.color_tag_purple)
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_red),
                        name = context.getString(R.string.color_tag_red)
                    ),
                    ColorTag(
                        color = context.getColorString(R.color.color_tag_yellow),
                        name = context.getString(R.string.color_tag_yellow)
                    )
                ).reversed()
                repository.colorTagDao.insert(initialData)
            }
        }
    }
}
