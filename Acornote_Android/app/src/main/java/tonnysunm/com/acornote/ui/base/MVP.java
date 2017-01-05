package tonnysunm.com.acornote.ui.base;

public interface MVP {

    interface Presenter <V extends View> {
        void attachView(V view);

        void detachView();
    }

    interface View <P extends Presenter> {
        void setPresenter(P presenter);
    }

}
