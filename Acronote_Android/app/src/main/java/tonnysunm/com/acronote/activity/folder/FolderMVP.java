package tonnysunm.com.acronote.activity.folder;

import java.util.List;

import tonnysunm.com.acronote.model.Item;
import tonnysunm.com.acronote.mvp.MVP;

public interface FolderMVP {

    interface Presenter {
        void loadData(int folderId);
    }

    interface View extends MVP.View<FolderPresenter> {
        void refresh(List<Item> items);
    }
}