package tonnysunm.com.acornote.ui.home;

import java.util.List;

import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.base.MVP;

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
