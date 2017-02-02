package tonnysunm.com.acornote.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import tonnysunm.com.acornote.AcornoteApplication;
import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.ui.editfolder.EditFolderViewModel;

public class Folder extends RealmObject implements Parcelable {
    private static final String TAG = "Folder";

    @PrimaryKey
    public int id;

    public String title;
    public String colorName;
    public String url;

    public boolean flashcardable;
    public boolean audioPlayable;

    public boolean markable;

    public Date updatedAt;
    public Date createdAt;

    public Folder(){ }

    static public void createInitialData(Realm realm) {
        final Resources res = AcornoteApplication.getContext().getResources();
        String[] titles = res.getStringArray(R.array.folderTitlesDemo);
        String[] colors = res.getStringArray(R.array.folderColorKeys);

        int folderId = 1;
        int itemId = 1;
        for (String title : titles) {
            Folder folder = realm.createObject(Folder.class, folderId++);
            folder.url = "https://baidu.com";
            folder.audioPlayable = true;
            folder.flashcardable = true;
            folder.title = title;
            String color = colors[new Random().nextInt(colors.length)];
            folder.colorName = color;
            folder.updatedAt = new Date();
            folder.createdAt = new Date();

            String[] titles1 = res.getStringArray(R.array.itemTitlesDemo);

            for (String item1 : titles1) {
                Item item = realm.createObject(Item.class, itemId++);
                item.createdAt = new Date();
                item.title = item1;
                item.des = String.format("des %d", itemId);
                item.imgUrl = "http://weknowyourdreams.com/images/beautiful/beautiful-03.jpg";
                item.folder = folder;
            }
        }
    }

    //CRUD
    public static void findAllAsync(RealmChangeListener<RealmResults<Folder>> listener) {
        RealmResults<Folder> result = AcornoteApplication.REALM.where(Folder.class).findAllSortedAsync("updatedAt", Sort.DESCENDING);
        result.addChangeListener(listener);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.colorName);
        dest.writeString(this.url);

        dest.writeByte((byte) (flashcardable ? 1 : 0));
        dest.writeByte((byte) (audioPlayable ? 1 : 0));
    }

    protected Folder(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.colorName = in.readString();
        this.url = in.readString();

        this.flashcardable = in.readByte() != 0;
        this.audioPlayable = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Folder> CREATOR = new Parcelable.Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    static public int colorByName(Context context, String name) {
        if (name == null) {
            return context.getResources().getIntArray(R.array.folderColorsValues)[0];
        }

        int i = -1;
        for (String text: context.getResources().getStringArray(R.array.folderColorKeys)) {
            i++;
            if (text.equals(name))
                break;
        }
        return context.getResources().getIntArray(R.array.folderColorsValues)[i];
    }

    static public int colorByIndex(Context context, int index) {
        return context.getResources().getIntArray(R.array.folderColorsValues)[index];
    }

    static public void createFolder(EditFolderViewModel model, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        AcornoteApplication.REALM.executeTransactionAsync((realm) -> {
                    final int id = realm.where(Folder.class).max("id").intValue() + 1;
                    final Folder f = realm.createObject(Folder.class, id);
                    f.createdAt = new Date();

                    f.updateFolderWithViewModel(model);
                },
                onSuccess,
                onError);
    }

    static public void updateFolder(EditFolderViewModel model, int folderId, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        AcornoteApplication.REALM.executeTransactionAsync((realm) -> {
                    final Folder f = realm.where(Folder.class).equalTo("id", folderId).findFirst();
                    f.updateFolderWithViewModel(model);
                },
                onSuccess,
                onError);
    }

    private void updateFolderWithViewModel(EditFolderViewModel model) {
        title = model.title;
        colorName = model.colorName;
        url = model.url;

        audioPlayable = model.audioPlayable;
        flashcardable = model.flashcardable;
        markable = model.markable;

        updatedAt = new Date();
    }
}
