package tonnysunm.com.acronote.model;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import tonnysunm.com.acronote.AcronoteApplication;

/**
 * Created by Tonny on 30/12/16.
 */

public class Item extends RealmObject {
    @PrimaryKey
    public int id;

    public String title;
    public String imgUrl;
    public String url;

    public Folder folder;

    public Item() {}

    public Item(int id, String title, String imgUrl, String url, Folder folder) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.url = url;
        this.folder = folder;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean hasImage() { return imgUrl != null; }

    public boolean hasUrl() {
        return url != null;
    }

    //CRUD
    public static void findAllAsync(RealmChangeListener<RealmResults<Item>> listener) {
        RealmResults<Item> result = AcronoteApplication.REALM.where(Item.class).findAllAsync();
        result.addChangeListener(listener);
    }

    public static void findAllInFolderAsync(int folderId, RealmChangeListener<RealmResults<Item>> listener) {
        if (folderId == 0) {
            findAllAsync(listener);
            return;
        }

        RealmResults<Item> result = AcronoteApplication.REALM.where(Item.class)
                .equalTo("folder.id", folderId)
                .findAllAsync();

        result.addChangeListener(listener);
    }
}
