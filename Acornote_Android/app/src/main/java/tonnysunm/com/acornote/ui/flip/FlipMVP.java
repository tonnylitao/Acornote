package tonnysunm.com.acornote.ui.flip;

import java.util.List;

import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.base.MVP;

/**
 * Created by Tonny on 20/12/16.
 *
 * business logic
 */

public interface FlipMVP {

    interface Presenter {
        void loadData();
    }

    interface View extends MVP.View<FlipPresenter> {
        void refresh(List<Folder> folders);
    }
}
