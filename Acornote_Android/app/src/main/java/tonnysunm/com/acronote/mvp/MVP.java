package tonnysunm.com.acronote.mvp;

public interface MVP {

    interface Presenter {
        void destroy();

        void setView(View view);
    }

    interface View <P extends Presenter> {
        void setPresenter(P presenter);
    }

}
