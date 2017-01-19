package tonnysunm.com.acornote.ui.home;

import android.content.Context;
import android.databinding.BaseObservable;

import tonnysunm.com.acornote.AcornoteApplication;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.model.Item;

/**
 * Created by Tonny on 1/01/17.
 */

public class FolderViewModel extends BaseObservable {
    public Folder folder;

    private Context mContext;

    public FolderViewModel setContext(Context context) {
        mContext = context;
        return this;
    }

    //value changed
    //public ObservableField<String> ownerName;

    //note: networking logic async operation should stay in presenter, not in ViewModel

    public FolderViewModel(Folder folder) {
        this.folder = folder;
        notifyChange();
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
        notifyChange();
    }

    public int getColor() {
        return Folder.colorByName(mContext, folder.colorName);
    }

    public String getItemCount() {
        final int count = AcornoteApplication.REALM.where(Item.class).equalTo("folder.id", folder.id).findAll().size();
        return String.format("%d items", count);
    }

}
