package tonnysunm.com.acornote.ui.editfolder;

import tonnysunm.com.acornote.ui.base.BasePresenter;

class EditFolderPresenter extends BasePresenter<EditFolderMVP.View> implements EditFolderMVP.Presenter {
    private static final String TAG = EditFolderPresenter.class.getSimpleName();

    @Override
    public void loadData() {
        //async operations
        getMVPView().refresh("Hello MVP");
    }

}