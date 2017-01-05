package tonnysunm.com.acornote.ui.folder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.databinding.FolderFragmentBinding;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.model.Item;

import static com.google.common.base.Preconditions.checkNotNull;

public class FolderFragment extends Fragment implements FolderMVP.View {
    private static final String TAG = FolderFragment.class.getSimpleName();

    private FolderPresenter mPresenter;

    private Folder mFolder;

    public FolderFragment() {}

    @Override
    public void setPresenter(FolderPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.folder_fragment, container, false);

        final Bundle bundle = getActivity().getIntent().getExtras();
        mFolder = bundle.getParcelable(FolderActivity.EXTRA_FOLDER);

        //
        FolderFragmentBinding binding = FolderFragmentBinding.bind(view);
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(null));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkNotNull(mPresenter, "mPresenter should not be null");

        final FolderAdapter adapter = new FolderAdapter();
        getRecycleView().setAdapter(adapter);
    }

    private RecyclerView getRecycleView() {
        final RecyclerView recyclerView = (RecyclerView) getView();

        return checkNotNull(recyclerView, "recyclerView should not be null");
    }

    @Override
    public void refresh(List<Item> list) {
        Log.d(TAG, list.toString());

        final RecyclerView recyclerView = getRecycleView();
        ((FolderAdapter)recyclerView.getAdapter()).swap(list);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, String.format("folderId = %d", mFolder.id));

        mPresenter.loadData(mFolder.id);
    }
}