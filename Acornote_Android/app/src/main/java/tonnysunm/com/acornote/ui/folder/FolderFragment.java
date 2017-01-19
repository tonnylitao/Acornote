package tonnysunm.com.acornote.ui.folder;

import android.content.Context;
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
import tonnysunm.com.acornote.databinding.FolderFragmentBinding;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.edititem.EditItemActivity;
import tonnysunm.com.acornote.ui.flip.FlipActivity;

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

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.folder_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final MenuItem flip = menu.findItem(R.id.menu_flip);
        flip.setVisible(mFolder.flashcardable);

        final MenuItem audio = menu.findItem(R.id.menu_play);
        audio.setVisible(mFolder.audioPlayable);

        final MenuItem url = menu.findItem(R.id.menu_url);
        url.setVisible(mFolder.url != null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_flip:
                final Context ctx = getContext();

                //TODO find visible first item
                startActivity(FlipActivity.createIntent(ctx, null, mFolder));
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void addItem() {
        final Context ctx = getContext();
        startActivityForResult(EditItemActivity.createIntent(ctx, null, mFolder), EditItemActivity.CREATE_ITEM_REQUEST);
    }

    @Override
    public void editItem(ItemViewModel model) {
        final Context ctx = getContext();
        startActivityForResult(EditItemActivity.createIntent(ctx, model.item, mFolder), EditItemActivity.EDIT_ITEM_REQUEST);
    }
}