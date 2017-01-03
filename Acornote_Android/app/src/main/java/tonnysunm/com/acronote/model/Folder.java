package tonnysunm.com.acronote.model;

import android.content.res.Resources;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import tonnysunm.com.acronote.AcronoteApplication;
import tonnysunm.com.acronote.R;

/**
 * Created by Tonny on 29/12/16.
 */

public class Folder extends RealmObject {
    public final static String EXTRA_FOLDER_ID = "tonnysunm.com.acronote.model.folder.id";

    @PrimaryKey
    public int id;

    public String title;
    public String color;

    public Folder(){
        super();
    }

    public Folder(int id, String title, String color) {
        this.id = id;
        this.title = title;
        this.color = color;
    }

    //getter and setter

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    static public void createInitialData(Realm realm) {
        final Resources res = AcronoteApplication.getContext().getResources();
        String[] titles = res.getStringArray(R.array.folderTitlesDemo);
        String[] colors = res.getStringArray(R.array.folderColors);

        int folderId = 1;
        int itemId = 1;
        for (String title : titles) {
            Folder folder = realm.createObject(Folder.class, folderId++);
            folder.setTitle(title);
            String color = colors[new Random().nextInt(colors.length)];
            folder.setColor(color);

            String[] titles1 = res.getStringArray(R.array.itemTitlesDemo);

            for (String item1 : titles1) {
                Item item = realm.createObject(Item.class, itemId++);
                item.setTitle(item1);
                item.setFolder(folder);
            }
        }
    }

    //CRUD
    public static void findAllAsync(RealmChangeListener<RealmResults<Folder>> listener) {
        RealmResults<Folder> result = AcronoteApplication.REALM.where(Folder.class).findAllAsync();
        result.addChangeListener(listener);
    }

}
