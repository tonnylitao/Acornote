package tonnysunm.com.acornote.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.databinding.HomeFragmentBinding;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.editfolder.EditFolderActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class HomeFragment extends Fragment implements HomeMVP.View {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomePresenter mPresenter;

    public HomeFragment() {}

    @Override
    public void setPresenter(HomePresenter presenter) {
        mPresenter = presenter;
    }

    /***** Lifecycle *****/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

//        view.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
//        view.setOnRefreshListener(() -> {
//            mPresenter.createFolder();
//            view.setRefreshing(false);
//        });

        //
        HomeFragmentBinding binding = HomeFragmentBinding.bind(view);
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                final Context ctx = getActivity();
                startActivityForResult(EditFolderActivity.createIntent(ctx, null), EditFolderActivity.CREATE_FOLDER_REQUEST);
                break;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkNotNull(mPresenter, "mPresenter should not be null");

        final HomeAdapter adapter = new HomeAdapter();
        getRecycleView().setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.loadData();
    }

    /***** HomeMVP.View *****/

    @Override
    public void refresh(List<Folder> folders) {
        final RecyclerView recyclerView = getRecycleView();
        ((HomeAdapter)recyclerView.getAdapter()).swap(folders);
    }

    /***** Helper *****/

    private RecyclerView getRecycleView() {
        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        return recyclerView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditFolderActivity.CREATE_FOLDER_REQUEST) {
            if (requestCode == Activity.RESULT_CANCELED) {

            }else if (requestCode == Activity.RESULT_OK) {
                Log.d(TAG, data.getData().toString());
            }
        }
    }
}
