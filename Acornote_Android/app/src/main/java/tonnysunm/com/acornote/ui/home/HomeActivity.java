package tonnysunm.com.acornote.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.ui.base.BaseActivity;
import static com.google.common.base.Preconditions.checkNotNull;

public class HomeActivity extends BaseActivity<HomePresenter> {
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);

        final HomeFragment fragment = (HomeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        checkNotNull(fragment, "view should not be null");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayUseLogoEnabled(true);

        mPresenter = new HomePresenter();
        mPresenter.attachView(fragment);
        fragment.setPresenter(mPresenter);
    }
}
