package tonnysunm.com.acornote.ui.edititem;

import tonnysunm.com.acornote.ui.base.MVP;

public interface EditItemMVP {

    interface View extends MVP.View<MVP.Presenter> {
        void onCancel();
        void onSure(EditItemViewModel viewModel);

        void previous();
        void next();

        void add();
    }
}