package tonnysunm.com.acornote.ui.editfolder;

import tonnysunm.com.acornote.ui.base.MVP;

public interface EditFolderMVP {

    interface View extends MVP.View<MVP.Presenter> {
        void onCancel();
        void onSure(EditFolderViewModel viewModel);
    }
}