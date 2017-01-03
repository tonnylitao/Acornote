package tonnysunm.com.acornote.activity.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.mvp.BaseActivity;
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

        mPresenter = new HomePresenter();
        mPresenter.setView(fragment);
        fragment.setPresenter(mPresenter);
    }
}
