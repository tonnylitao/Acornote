package tonnysunm.com.acornote.ui.base;

import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<P extends MVP.Presenter> extends AppCompatActivity implements MVP {

    protected P mPresenter;

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        super.onDestroy();
    }

}