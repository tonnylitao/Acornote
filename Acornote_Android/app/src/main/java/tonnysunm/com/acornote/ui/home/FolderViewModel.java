package tonnysunm.com.acornote.ui.home;

import android.content.Context;
import android.databinding.BaseObservable;

import tonnysunm.com.acornote.model.Folder;

/**
 * Created by Tonny on 1/01/17.
 */

public class FolderViewModel extends BaseObservable {
    private Folder mFolder;

    public Folder getFolder() {
        return mFolder;
    }

    //resource
    private Context mContext;

    public FolderViewModel setContext(Context context) {
        mContext = context;
        return this;
    }

    //value changed
    //public ObservableField<String> ownerName;

    //note: networking logic async operation should stay in presenter, not in ViewModel

    public FolderViewModel(Folder folder) {
        this.mFolder = folder;
        notifyChange();
    }

    public int getId() {
        return  mFolder.id;
    }

    public void setFolder(Folder folder) {
        this.mFolder = folder;
    }

    public String getTitle() {
        return mFolder.title;
    }

    public int getColor() {
        return Folder.colorByName(mContext, mFolder.colorName);
    }
}
