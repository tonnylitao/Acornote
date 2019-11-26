package tonnysunm.com.acornote.ui.note

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tonnysunm.com.acornote.ui.note.edit.EditNoteActivity
import tonnysunm.com.acornote.MainActivity
import tonnysunm.com.acornote.R
import tonnysunm.com.acornote.SharedViewModel
import tonnysunm.com.acornote.databinding.FragmentNoteBinding
import tonnysunm.com.acornote.model.EmptyId
import tonnysunm.com.acornote.model.NoteFilter

class NoteListFragment : Fragment() {
    private lateinit var mainModel: SharedViewModel

    private val mArrowDrawable: DrawerArrowDrawable by lazy {
        DrawerArrowDrawable(context).apply { color = Color.WHITE }
    }

    private val mViewModel: NoteListViewModel by viewModels {
        val filter = arguments?.getString("filter") ?: ""
        val folderId = arguments?.getLong(getString(R.string.folderIdKey)) ?: 0
        val folderTitle = arguments?.getString("folderTitle") ?: ""

        val noteFilter = when {
            filter == "favourite" -> NoteFilter.Favourite
            folderId > 0 -> NoteFilter.ByFolder(folderId, folderTitle)
            else -> NoteFilter.All
        }

        DetailViewModelFactory(requireActivity().application, noteFilter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = this.mViewModel

        val adapter = NoteListAdapter()
        mViewModel.data.observe(this.viewLifecycleOwner, Observer {
            Log.d("TAG", "$it")
            adapter.submitList(it)
        })
        binding.recyclerview.adapter = adapter

        binding.setOnAddNote {

            val intent = Intent(activity, EditNoteActivity::class.java).apply {
                val noteFilter = mViewModel.noteFilterLiveData.value
                putExtra(getString(R.string.folderIdKey), noteFilter?.folderId ?: EmptyId)

                if (noteFilter == NoteFilter.Favourite) {
                    putExtra("favourite", true)
                }
            }

            startActivity(intent)
        }

        return binding.root
    }

    //
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity as? MainActivity ?: throw Exception("Invalid Activity")
        val actionBar = activity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val delegate = (activity as AppCompatActivity).drawerToggleDelegate
        delegate?.setActionBarUpIndicator(
            mArrowDrawable,
            androidx.navigation.ui.R.string.nav_app_bar_open_drawer_description
        )
        mArrowDrawable.progress = 0f

        //
        mainModel = ViewModelProvider(activity).get(SharedViewModel::class.java)

        mainModel.noteFilterLiveData.observe(this.viewLifecycleOwner, Observer {
            mViewModel.noteFilterLiveData.value = it

            actionBar?.title = it.title
        })
    }


}