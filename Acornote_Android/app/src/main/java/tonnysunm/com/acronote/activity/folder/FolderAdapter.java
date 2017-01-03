package tonnysunm.com.acronote.activity.folder;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tonnysunm.com.acronote.databinding.FolderItemviewBinding;
import tonnysunm.com.acronote.model.Item;

/**
 * Created by Tonny on 31/12/16.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private static final String TAG = FolderAdapter.class.getSimpleName();

    private List<Item> mDataSource;

    @Override
    public int getItemCount() {
        return mDataSource==null ? 0 : mDataSource.size();
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
        if (mDataSource == null) {
            mDataSource = new ArrayList<Item>();
        }

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
            mBinding.setViewModel(new ItemViewModel(item));
            mBinding.executePendingBindings();
        }
    }

    @BindingAdapter("android:layout_marginRight")
    public static void setRightMargin(View view, float margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                (int)margin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }
}
