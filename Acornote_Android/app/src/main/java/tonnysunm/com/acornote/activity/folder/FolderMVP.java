package tonnysunm.com.acornote.activity.folder;

import java.util.List;

import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.mvp.MVP;

public interface FolderMVP {

    interface Presenter {
        void loadData(int folderId);
    }

    interface View extends MVP.View<FolderPresenter> {
        void refresh(List<Item> items);
    }
}