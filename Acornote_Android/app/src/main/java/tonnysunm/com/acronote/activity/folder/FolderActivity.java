package tonnysunm.com.acronote.activity.folder;

import android.os.Bundle;
import android.support.annotation.Nullable;

import tonnysunm.com.acronote.R;
import tonnysunm.com.acronote.mvp.BaseActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FolderActivity extends BaseActivity<FolderPresenter> {
    private static final String TAG = FolderActivity.class.getSimpleName();

    private int folderId;

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