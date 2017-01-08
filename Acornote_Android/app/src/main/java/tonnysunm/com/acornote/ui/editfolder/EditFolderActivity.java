package tonnysunm.com.acornote.ui.editfolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.base.BaseActivity;
import tonnysunm.com.acornote.ui.base.MVP;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditFolderActivity extends BaseActivity<MVP.Presenter> {
    public static final int CREATE_FOLDER_REQUEST = 1;
    public static final String EXTRA_FOLDER = "EXTRA_FOLDER";
    public static final String EXTRA_FOLDER_VIEWMODEL = "EXTRA_FOLDER_VIEWMODEL";

    private static final String TAG = EditFolderActivity.class.getSimpleName();

    public static Intent createIntent(Context context, @Nullable Folder folder) {
        Intent intent = new Intent(context, EditFolderActivity.class);
        if (folder != null) {
            intent.putExtra(EXTRA_FOLDER, folder);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.editfolder_activity);

        final EditFolderFragment fragment = (EditFolderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        checkNotNull(fragment, "view should not be null");
    }
}