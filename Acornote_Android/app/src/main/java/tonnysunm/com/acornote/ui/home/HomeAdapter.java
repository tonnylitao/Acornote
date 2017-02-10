package tonnysunm.com.acornote.ui.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tonnysunm.com.acornote.databinding.HomeCardviewBinding;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.folder.FolderActivity;

/**
 * Created by Tonny on 29/12/16.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private static final String TAG = HomeAdapter.class.getSimpleName();

    private final List<Folder> mFolders;

    HomeAdapter() {
        mFolders = new ArrayList<Folder>();
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return ViewHolder.create(LayoutInflater.from(viewGroup.getContext()), viewGroup);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mFolders.get(position));
    }

    public void swap(List<Folder> datas){
        mFolders.clear(); //GC
        mFolders.addAll(datas);
        notifyDataSetChanged();
    }

    public void removeFolderAt(int position) {
        mFolders.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mFolders.size());
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private HomeCardviewBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        private ViewHolder(@NonNull HomeCardviewBinding binding) {
            super(binding.getRoot());

            itemView.setOnClickListener((v) -> {
                final Context ctx = v.getContext();

                final HomeCardviewBinding bindingInner = DataBindingUtil.getBinding(v);
                final Folder folder = bindingInner.getViewModel().folder;

                Log.d(TAG, folder.title);
                ctx.startActivity(FolderActivity.createIntent(ctx, folder));
            });
            mBinding = binding;
        }

        static ViewHolder create(LayoutInflater inflater, ViewGroup viewGroup) {
            final HomeCardviewBinding binding = HomeCardviewBinding.inflate(inflater, viewGroup, false);
            return new ViewHolder(binding);
        }

        public void bind(Folder folder) {
            final FolderViewModel viewModel = mBinding.getViewModel();
            if (viewModel == null) {
                final FolderViewModel model = new FolderViewModel(folder);
                model.setContext(mBinding.getRoot().getContext());
                mBinding.setViewModel(model);
            }else {
                viewModel.setFolder(folder);
            }
        }

    }
}