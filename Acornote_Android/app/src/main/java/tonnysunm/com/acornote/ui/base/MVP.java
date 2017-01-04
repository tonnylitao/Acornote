package tonnysunm.com.acornote.ui.base;

public interface MVP {

    interface Presenter {
        void destroy();

        void setView(View view);
    }

    interface View <P extends Presenter> {
        void setPresenter(P presenter);
    }

}
