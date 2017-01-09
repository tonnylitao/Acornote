package tonnysunm.com.acornote.ui.edititem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.base.BaseActivity;
import tonnysunm.com.acornote.ui.base.MVP;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditItemActivity extends BaseActivity<MVP.Presenter> {
    public static final int CREATE_ITEM_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;

    public static final String EXTRA_FOLDER_VIEWMODEL = "EXTRA_FOLDER_VIEWMODEL";

    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    public static final String EXTRA_COLOR_NAME = "EXTRA_COLOR_NAME";
    public static final String EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID";

    private static final String TAG = EditItemActivity.class.getSimpleName();

    public static Intent createIntent(Context context, @Nullable Item item, @NonNull Folder folder) {
        Intent intent = new Intent(context, EditItemActivity.class);
        if (item != null) {
            intent.putExtra(EXTRA_ITEM, item);
        }
        intent.putExtra(EXTRA_FOLDER_ID, folder.id);
        intent.putExtra(EXTRA_COLOR_NAME, folder.colorName);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edititem_activity);

        final EditItemFragment fragment = (EditItemFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        checkNotNull(fragment, "view should not be null");
    }
}