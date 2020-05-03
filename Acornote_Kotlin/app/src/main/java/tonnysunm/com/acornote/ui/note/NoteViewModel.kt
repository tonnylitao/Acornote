package tonnysunm.com.acornote.ui.note

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.*
import java.util.*

private val TAG = "NoteViewModel"

class NoteViewModel(application: Application, private val intent: Intent) :
    AndroidViewModel(application) {

    private val _repository: Repository by lazy { Repository(application) }

    private val isCreateNewNote: Boolean
        get() {
            val id = intent.getIntExtra("id", EmptyId)
            return id == EmptyId
        }

    val data: LiveData<NoteWithImages> by lazy {
        val id = intent.getIntExtra("id", EmptyId)

        if (id > EmptyId) {
            _repository.noteDao.noteWithImages(id)
        } else {
            val text: String? = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)

            val textRemoveMediumLink = text?.let {
                val regex = Regex("^“(.*)” by  https://link.medium.com/")
                val match = regex.find(it)
                match?.groups?.last()?.value ?: it
            }

            val star = intent.getBooleanExtra("star", false)

            _repository.noteDao.noteEditingWithImages().switchMap {
                if (it == null) {
                    val note = Note(
                        title = "",
                        order = 0,
                        star = star,
                        editing = true
                    )

                    if (!textRemoveMediumLink.isNullOrEmpty()) {
                        if (textRemoveMediumLink.textAsTitle()) {
                            note.title = textRemoveMediumLink
                        } else {
                            note.description = textRemoveMediumLink
                        }
                    }

                    viewModelScope.launch(Dispatchers.IO) {
                        note.order = (_repository.noteDao.maxOrder() ?: 0) + 1

                        val newId = _repository.noteDao.insert(note).toInt()
                        data.value?.note?.id = newId

                        val sharedPref =
                            application.getSharedPreferences("acronote", Context.MODE_PRIVATE)
                        val prefLabelId = sharedPref.getInt("default_label_id", 0)

                        var labelId = intent.getIntExtra("labelId", 0)
                        if (labelId == 0) {
                            labelId = prefLabelId
                        }

                        if (labelId > 0) {
                            try {
                                _repository.noteLabelDao.insert(
                                    NoteLabelCrossRef(noteId = newId, labelId = labelId)
                                )
                            } catch (e: Exception) {
                            }
                        }
                    }

                    MutableLiveData(NoteWithImages(note, null))
                } else {
                    if (!textRemoveMediumLink.isNullOrEmpty()) {
                        if (textRemoveMediumLink.textAsTitle()) {
                            it.note.title = textRemoveMediumLink
                            it.note.description = null
                        } else {
                            it.note.title = ""
                            it.note.description = textRemoveMediumLink
                        }
                    }

                    MutableLiveData(it)
                }
            }
        }
    }

    val savable = data.switchMap {
        MutableLiveData(it.note.title.isNotEmpty())
    }

    fun onTitleChanged(text: CharSequence) {
        (savable as? MutableLiveData<Boolean>)?.value = text.isNotEmpty()
    }

    suspend fun updateNote() {
        val note = data.value?.note ?: throw IllegalStateException("note is not set")

        note.updatedAt = Date().time
        _repository.noteDao.update(note)
    }

    suspend fun updateColorTag(colorTag: ColorTag) {
        val note = data.value?.note ?: throw IllegalStateException("note is not set")

        if (isCreateNewNote) {
            note.colorTag = colorTag
            (data as? MutableLiveData<Note>)?.postValue(note)
        } else {
            note.colorTag = colorTag
            _repository.noteDao.update(note)
        }
    }

    fun deleteNote() {
        data.value?.note?.let {
            viewModelScope.launch(Dispatchers.IO) {
                _repository.noteDao.delete(it)
            }
        }
    }

    fun createNote(text: String, block: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            var tips = ""
            try {
                if (_repository.noteDao.getCountByString(text) > 0) {
                    tips = "Repeated"
                } else {
                    val order = (_repository.noteDao.maxOrder() ?: 0) + 1

                    val note = Note(
                        title = text,
                        order = order,
                        editing = false
                    )

                    val newId = _repository.noteDao.insert(note).toInt()

                    val sharedPref =
                        getApplication<Application>().getSharedPreferences(
                            "acronote",
                            Context.MODE_PRIVATE
                        )
                    val labelId = sharedPref.getInt("default_label_id", 0)

                    if (labelId > 0) {
                        _repository.noteLabelDao.insert(
                            NoteLabelCrossRef(noteId = newId, labelId = labelId)
                        )
                    }
                    tips = "Save Success"
                }

            } catch (e: Exception) {
                Log.d(TAG, e.toString())
                tips = "Save Failed"
            } finally {
                viewModelScope.launch(Dispatchers.Main) {
                    block(tips)
                }
            }
        }
    }
}

inline fun <S, T> dependantLiveData(
    vararg dependencies: LiveData<out S>,
    defaultValue: T? = null,
    crossinline mapper: (S) -> T?
): LiveData<T> =
    MediatorLiveData<T>().also { mediatorLiveData ->
        dependencies.forEach { dependencyLiveData ->
            mediatorLiveData.addSource(dependencyLiveData) {
                mediatorLiveData.value = mapper(it)
            }
        }
    }.apply { value = defaultValue }