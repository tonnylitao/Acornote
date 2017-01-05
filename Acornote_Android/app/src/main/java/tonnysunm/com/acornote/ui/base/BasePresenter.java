package tonnysunm.com.acornote.ui.base;

public abstract class BasePresenter<V extends MVP.View> implements MVP.Presenter<V> {
    private V mView;

    public V getMVPView() {
        return mView;
    }

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

}
