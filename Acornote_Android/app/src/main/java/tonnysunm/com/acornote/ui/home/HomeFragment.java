package tonnysunm.com.acornote.ui.home;

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
import tonnysunm.com.acornote.databinding.HomeFragmentBinding;
import tonnysunm.com.acornote.model.Folder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Tonny on 23/12/16.
 */

public class HomeFragment extends Fragment implements HomeMVP.View {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomePresenter mPresenter;

    public HomeFragment() {}

    @Override
    public void setPresenter(HomePresenter presenter) {
        mPresenter = presenter;
    }

    //lifecycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        HomeFragmentBinding binding = HomeFragmentBinding.bind(view);
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        recyclerView.setHasFixedSize(true);

        return view;
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

    //HomeMVP.View

    @Override
    public void refresh(List<Folder> folders) {
        final RecyclerView recyclerView = getRecycleView();
        ((HomeAdapter)recyclerView.getAdapter()).swap(folders);
    }

    //Helper

    private RecyclerView getRecycleView() {
        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        return recyclerView;
    }

}
