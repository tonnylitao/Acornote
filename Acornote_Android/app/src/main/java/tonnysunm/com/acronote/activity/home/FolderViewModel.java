package tonnysunm.com.acronote.activity.home;

import tonnysunm.com.acronote.model.Folder;

/**
 * Created by Tonny on 1/01/17.
 */

public class FolderViewModel {
    Folder folder;

    public String title;

    public String getTitle() {
        return folder.title;
    }

    public FolderViewModel(Folder folder) {
        this.folder = folder;
    }
}
