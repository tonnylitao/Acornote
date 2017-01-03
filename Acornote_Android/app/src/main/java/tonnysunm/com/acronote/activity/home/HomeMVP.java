package tonnysunm.com.acronote.activity.home;

import java.util.List;

import tonnysunm.com.acronote.model.Folder;
import tonnysunm.com.acronote.mvp.MVP;

/**
 * Created by Tonny on 20/12/16.
 *
 * business logic
 */

public interface HomeMVP {

    interface Presenter {
        void loadData();
    }

    interface View extends MVP.View<HomePresenter> {
        void refresh(List<Folder> folders);
    }
}
