package tonnysunm.com.acornote.ui.base;

public abstract class BasePresenter<V extends MVP.View> implements MVP.Presenter {
    protected V mView;

    @Override
    @SuppressWarnings("unchecked")
    public void setView(MVP.View view) {
        mView = (V) view;
    }

    @Override
    public void destroy() {
        mView = null;
    }

}
