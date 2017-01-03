package tonnysunm.com.acornote.activity.folder;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import tonnysunm.com.acornote.databinding.FolderItemviewBinding;
import tonnysunm.com.acornote.model.Item;

/**
 * Created by Tonny on 31/12/16.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private static final String TAG = FolderAdapter.class.getSimpleName();

    private List<Item> mDataSource;

    FolderAdapter() {
        mDataSource = new ArrayList<Item>();
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return FolderAdapter.ViewHolder.create(LayoutInflater.from(viewGroup.getContext()), viewGroup);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.ViewHolder holder, int position) {
        holder.bind(mDataSource.get(position));
    }

    public void swap(List<Item> datas){
        mDataSource.clear(); //GC
        mDataSource.addAll(datas);
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
        private FolderItemviewBinding mBinding;

        private ViewHolder(@NonNull FolderItemviewBinding binding) {
            super(binding.getRoot());

            itemView.setOnClickListener((v) -> {

            });
            mBinding = binding;
        }

        static FolderAdapter.ViewHolder create(LayoutInflater inflater, ViewGroup viewGroup) {
            final FolderItemviewBinding binding = FolderItemviewBinding.inflate(inflater, viewGroup, false);
            return new FolderAdapter.ViewHolder(binding);
        }

        public void bind(Item item) {
            final ItemViewModel viewModel = mBinding.getViewModel();
            if (viewModel == null) {
                mBinding.setViewModel(new ItemViewModel(item));
            }else {
                viewModel.setItem(item);
            }
        }
    }

    @BindingAdapter("app:layout_marginRight")
    public static void setRightMargin(View view, float margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                (int)margin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("app:imageUrl")
    public static void setSrc(ImageView view, String url) {
        if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {

        }
//        Picasso.with(view.getContext())
//                .load(url)
//                .placeholder(R.drawable.placeholder)
//                .into(view);
    }

}
