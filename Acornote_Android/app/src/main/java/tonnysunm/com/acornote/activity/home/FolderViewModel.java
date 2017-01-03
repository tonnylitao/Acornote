package tonnysunm.com.acornote.activity.home;

import android.databinding.BaseObservable;

import tonnysunm.com.acornote.model.Folder;

/**
 * Created by Tonny on 1/01/17.
 */

public class FolderViewModel extends BaseObservable {
    Folder folder;

//    private Context context;
//    private Subscription subscription;
//    public ObservableField<String> ownerName;

    public FolderViewModel(Folder folder) {
        this.folder = folder;
        notifyChange();
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getTitle() {
        return folder.title;
    }
}
