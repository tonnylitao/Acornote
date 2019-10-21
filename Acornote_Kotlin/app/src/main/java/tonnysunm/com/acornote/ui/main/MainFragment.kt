package tonnysunm.com.acornote.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding =  MainFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.lifecycleOwner = this@MainFragment
            this.viewModel = this@MainFragment.viewModel
        }

        this.context?.let { ctx ->
            val adapter = FolderListAdapter(ctx)
            binding.recyclerview.adapter = adapter

            viewModel.data.observe(this.viewLifecycleOwner, Observer {
                it.let { adapter.setFolders(it) }
            })

            binding.recyclerview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val index = rv.indexOf(e)

                    if (index != null) {
                        val item = adapter.itemOf(index)
                        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(item.title)
                        rv.findNavController().navigate(action)

                        return false
                    }

                    return false
                }
            })
        }

        return binding.root
    }

}

fun RecyclerView.indexOf(e: MotionEvent): Int? {
    val childView = this.findChildViewUnder(e.x, e.y)

    if (childView == null || e.action != MotionEvent.ACTION_UP) {
        return null
    }

    val index = getChildAdapterPosition(childView)
    return if (index == RecyclerView.NO_POSITION) null else index
}