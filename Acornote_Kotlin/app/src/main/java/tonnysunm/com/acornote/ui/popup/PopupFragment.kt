package tonnysunm.com.acornote.ui.popup

import android.app.Activity
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.databinding.FragmentPopupBinding
import tonnysunm.com.acornote.library.AndroidViewModelFactory
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.textAsTitle
import tonnysunm.com.acornote.ui.label.LabelListActivity
import tonnysunm.com.acornote.ui.note.NoteViewModel


class PopupFragment : Fragment() {

    private var binding: FragmentPopupBinding? = null

    val viewModel by viewModels<NoteViewModel> {
        AndroidViewModelFactory(
            requireActivity().application,
            requireActivity().intent
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = this
        val binding = FragmentPopupBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = fragment
            viewModel = fragment.viewModel

            switchTexts = View.OnClickListener {
                val note = fragment.viewModel.data.value

                val text = note?.title
                note?.title = note?.description ?: ""
                note?.description = text

                //refresh UI
                viewModel = fragment.viewModel
            }

            editLabel = View.OnClickListener {
                val startForResult =
                    requireActivity().prepareCall(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        }
                    }
                startForResult(Intent(context, LabelListActivity::class.java).apply {
                    putExtra("id", fragment.viewModel.data.value?.id)
                })
            }

            editColor = View.OnClickListener {

            }

            save = View.OnClickListener {
                insertOrUpdateNote()
            }
        }

        setHasOptionsMenu(true)

        this.binding = binding
        return binding.root
    }

    private fun insertOrUpdateNote() {
        val note = viewModel.data.value ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                note.editing = note.title.isEmpty()
                viewModel.updateNote()

                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } catch (e: Exception) {
            } finally {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()
            }
        }
    }

    fun onWindowFocus() {
        val fragment = this
        viewModel.data.observe(viewLifecycleOwner, Observer {
            val text = getCopyText()
            if (it.title.isEmpty() && it.description == null && text != null) {
                if (text.textAsTitle()) {
                    it.title = text
                } else {
                    it.description = text
                }

                (viewModel.data as? MutableLiveData<Note>)?.postValue(it)
            }

            viewModel.data.removeObservers(fragment.viewLifecycleOwner)
        })
    }

    private fun getCopyText(): String? {
        val clipboard =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return null

        if (clipboard.hasPrimaryClip() &&
            clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true
        ) {
            return clipboard.primaryClip?.getItemAt(0)?.text.toString()
        }

        return null
    }
}