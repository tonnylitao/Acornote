package tonnysunm.com.acornote.ui.folder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.ui.base.BaseActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FolderActivity extends BaseActivity<FolderPresenter> {
    public static final String EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID";
    private static final String TAG = FolderActivity.class.getSimpleName();

    private int folderId;

    public static Intent createIntent(Context context, int folderId) {
        Intent intent = new Intent(context, FolderActivity.class);
        intent.putExtra(EXTRA_FOLDER_ID, folderId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.folder_activity);

        final FolderFragment fragment = (FolderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        checkNotNull(fragment, "view should not be null");

        //
        mPresenter = new FolderPresenter();
        mPresenter.setView(fragment);
        fragment.setPresenter(mPresenter);
    }
}