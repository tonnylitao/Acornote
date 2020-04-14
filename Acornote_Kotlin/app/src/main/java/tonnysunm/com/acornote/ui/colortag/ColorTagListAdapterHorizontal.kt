package tonnysunm.com.acornote.ui.colortag

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import tonnysunm.com.acornote.databinding.ListItemColortagHorizontalBinding
import tonnysunm.com.acornote.databinding.ListItemEditBinding
import tonnysunm.com.acornote.model.ColorTag

private const val ColorTagType = 0
private const val FooterType = 1

class ColorTagListAdapterHorizontal(var array: List<ColorTag>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == ColorTagType)
            ViewHolder(
                ListItemColortagHorizontalBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        else
            EditViewHolder(
                ListItemEditBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

    override fun getItemViewType(position: Int) =
        if (position < array.count()) ColorTagType else FooterType

    override fun getItemCount() = array.count() + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(array[position])
        }
    }

    /* ViewHolder */

    inner class ViewHolder(private val binding: ListItemColortagHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clickListener = View.OnClickListener {
                val data = binding.data ?: return@OnClickListener

                val fragment = it.findFragment<ColorTagListFragmentHorizontal>()
                fragment.navigateToNotesBy(data)
            }
        }

        fun bind(item: ColorTag) {
            binding.data = item
            binding.executePendingBindings()
        }
    }

    inner class EditViewHolder(binding: ListItemEditBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clickListener = View.OnClickListener {
                val fragment = it.findFragment<ColorTagListFragmentHorizontal>()
                val activity = fragment.activity ?: return@OnClickListener

                val startForResult =
                    activity.prepareCall(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        }
                    }
                startForResult(Intent(activity, ColorTagListActivity::class.java))
            }
        }
    }

}