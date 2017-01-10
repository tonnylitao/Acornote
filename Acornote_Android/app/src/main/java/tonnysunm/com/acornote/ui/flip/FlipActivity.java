package tonnysunm.com.acornote.ui.flip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.base.BaseActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlipActivity extends BaseActivity<FlipPresenter> {
    public static String EXTRA_ITEM = "EXTRA_ITEM";
    public static String EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID";
    public static String EXTRA_FOLDER_COLOR_NAME = "EXTRA_FOLDER_COLOR_NAME";

    private static final String TAG = FlipActivity.class.getSimpleName();

    public static Intent createIntent(Context context, Item item, Folder folder) {
        Intent intent = new Intent(context, FlipActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        intent.putExtra(EXTRA_FOLDER_ID, folder.id);
        intent.putExtra(EXTRA_FOLDER_COLOR_NAME, folder.colorName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flip_activity);

        final FlipFragment fragment = (FlipFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        checkNotNull(fragment, "view should not be null");

        mPresenter = new FlipPresenter();
        mPresenter.attachView(fragment);
        fragment.setPresenter(mPresenter);
    }
}
