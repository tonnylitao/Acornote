package tonnysunm.com.acornote.activity.folder;

import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.mvp.BasePresenter;

class FolderPresenter extends BasePresenter<FolderMVP.View> implements FolderMVP.Presenter {
    private static final String TAG = FolderPresenter.class.getSimpleName();

    @Override
    public void loadData(int folderId){
        Item.findAllInFolderAsync(folderId, result -> {
            mView.refresh(result);
        });
    }

}