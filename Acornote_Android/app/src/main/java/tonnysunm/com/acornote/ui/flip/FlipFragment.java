package tonnysunm.com.acornote.ui.flip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.editfolder.EditFolderActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlipFragment extends Fragment implements FlipMVP.View {
    private static final String TAG = FlipFragment.class.getSimpleName();

    private FlipPresenter mPresenter;

    private ViewPager mPager;

    private int mFolderId;

    private ItemPageAdapter mAdapter;

    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

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
        ButterKnife.bind(this, view);

        mAdapter = new ItemPageAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mProgressBar.setProgress(position+1);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkNotNull(mPresenter, "mPresenter should not be null");
    }

    @Override
    public void onStart() {
        super.onStart();

        final Bundle bundle = getActivity().getIntent().getExtras();

        mFolderId = bundle.getInt(FlipActivity.EXTRA_FOLDER_ID, 0);

        final String colorName = bundle.getString(FlipActivity.EXTRA_FOLDER_COLOR_NAME);
        final Context ctx = getContext();
        mProgressBar.setProgressTintList(ColorStateList.valueOf(Folder.colorByName(ctx, colorName)));
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.loadData(mFolderId);
    }

    /***** FlipMVP.View *****/

    @Override
    public void refresh(List<Item> items) {
        Log.d(TAG, "refresh");

        mProgressBar.setMax(items.size()+1);

        mAdapter.setDataSource(items);
    }

    @OnClick(R.id.btn_cancel)
    public void cancel(View view) {
        getActivity().finish();
    }

    @Override
    public void play() {

    }

    @Override
    public void bing() {

    }

    @Override
    public void edit() {

    }

    @Override
    public void setMarked(boolean marked) {

    }

    @Override
    public void more() {

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
