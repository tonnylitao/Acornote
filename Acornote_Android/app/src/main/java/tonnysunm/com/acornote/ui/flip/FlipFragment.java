package tonnysunm.com.acornote.ui.flip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.editfolder.EditFolderActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlipFragment extends Fragment implements FlipMVP.View {
    private static final String TAG = FlipFragment.class.getSimpleName();

    private FlipPresenter mPresenter;

    public FlipFragment() {}

    @Override
    public void setPresenter(FlipPresenter presenter) {
        mPresenter = presenter;
    }

    /***** Lifecycle *****/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View view = inflater.inflate(R.layout.flip_fragment, container, false);

//        view.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
//        view.setOnRefreshListener(() -> {
//            mPresenter.createFolder();
//            view.setRefreshing(false);
//        });

        //
//        FlipFragmentBinding binding = FlipFragmentBinding.bind(view);
//        final RecyclerView recyclerView = binding.recyclerView;
//        recyclerView.setLayoutManager(new LinearLayoutManager(null));
//        recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkNotNull(mPresenter, "mPresenter should not be null");

//        final FlipAdapter adapter = new FlipAdapter();
//        getRecycleView().setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.loadData();
    }

    /***** FlipMVP.View *****/

    @Override
    public void refresh(List<Folder> folders) {
        final RecyclerView recyclerView = getRecycleView();
//        ((FlipAdapter)recyclerView.getAdapter()).swap(folders);
    }

    /***** Helper *****/

    private RecyclerView getRecycleView() {
        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        return recyclerView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditFolderActivity.CREATE_FOLDER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }
}
