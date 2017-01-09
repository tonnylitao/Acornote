package tonnysunm.com.acornote.ui.folder;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.base.BaseActivity;
import tonnysunm.com.acornote.ui.editfolder.EditFolderActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FolderActivity extends BaseActivity<FolderPresenter> {
    public static final String EXTRA_FOLDER = "EXTRA_FOLDER";
    private static final String TAG = FolderActivity.class.getSimpleName();

    public static Intent createIntent(Context context, Folder folder) {
        Intent intent = new Intent(context, FolderActivity.class);
        intent.putExtra(EXTRA_FOLDER, folder);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.folder_activity);

        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        //
        final FolderFragment fragment = (FolderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        checkNotNull(fragment, "view should not be null");

        final Folder folder = getIntent().getExtras().getParcelable(EditFolderActivity.EXTRA_FOLDER);

        //
        mPresenter = new FolderPresenter(folder);
        mPresenter.attachView(fragment);
        fragment.setPresenter(mPresenter);

        //
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Folder.colorByName(this, folder.colorName)));
        fab.setOnClickListener((v)->{
            fragment.addItem();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}