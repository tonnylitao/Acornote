package tonnysunm.com.acornote.ui.folder;

import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.base.BasePresenter;

class FolderPresenter extends BasePresenter<FolderMVP.View> implements FolderMVP.Presenter {
    private static final String TAG = FolderPresenter.class.getSimpleName();

    @Override
    public void loadData(int folderId){
        Item.findAllInFolderAsync(folderId, result -> {
            mView.refresh(result);
        });
    }

}