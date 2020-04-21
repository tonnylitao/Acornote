package tonnysunm.com.acornote.ui.popup

import android.app.Activity
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.databinding.FragmentPopupBinding
import tonnysunm.com.acornote.ui.label.LabelListActivity
import tonnysunm.com.acornote.ui.note.EditNoteViewModelFactory
import tonnysunm.com.acornote.ui.note.NoteViewModel


class PopupFragment : Fragment(R.layout.fragment_popup) {

    private var binding: FragmentPopupBinding? = null

    val viewModel: NoteViewModel by viewModels {
        EditNoteViewModelFactory(requireActivity().application, requireActivity().intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPopupBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.editLabel = View.OnClickListener {
            val startForResult =
                requireActivity().prepareCall(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    }
                }
            startForResult(Intent(context, LabelListActivity::class.java).apply {
                putExtra("id", viewModel.data.value?.id)
            })
        }

        binding.editColor = View.OnClickListener {

        }

        binding.save = View.OnClickListener {
            insertOrUpdateNote()
        }

        setHasOptionsMenu(true)

        this.binding = binding
        return binding.root
    }

    fun insertOrUpdateNote() {
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

}
