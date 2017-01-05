package tonnysunm.com.acornote.ui.editfolder;

import tonnysunm.com.acornote.ui.base.MVP;

public interface EditFolderMVP {

    interface Presenter {
        void loadData();
    }

    interface View extends MVP.View<EditFolderPresenter> {
        void onCancel();
        void onSure(EditFolderViewModel viewModel);

        void refresh(String string);
    }
}