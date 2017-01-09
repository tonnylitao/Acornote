package tonnysunm.com.acornote.ui.flip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.base.BaseActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlipActivity extends BaseActivity<FlipPresenter> {
    public static String EXTRA_ITEM = "EXTRA_ITEM";
    private static final String TAG = FlipActivity.class.getSimpleName();

    public static Intent createIntent(Context context, Item item) {
        Intent intent = new Intent(context, FlipActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flip_activity);

        final FlipFragment fragment = (FlipFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        checkNotNull(fragment, "view should not be null");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayUseLogoEnabled(true);

        mPresenter = new FlipPresenter();
        mPresenter.attachView(fragment);
        fragment.setPresenter(mPresenter);
    }
}
