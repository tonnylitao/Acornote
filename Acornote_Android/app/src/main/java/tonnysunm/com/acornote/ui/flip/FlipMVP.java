package tonnysunm.com.acornote.ui.flip;

import java.util.List;

import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.base.MVP;

/**
 * Created by Tonny on 20/12/16.
 *
 * business logic
 */

public interface FlipMVP {

    interface Presenter {
        void loadData(int folderId);
    }

    interface View extends MVP.View<FlipPresenter> {
        void refresh(List<Item> items);

        void play();
        void bing();
        void edit();
        void setMarked(boolean marked);
        void more();

        void showUrl(FlipItemViewModel model);
    }
}
