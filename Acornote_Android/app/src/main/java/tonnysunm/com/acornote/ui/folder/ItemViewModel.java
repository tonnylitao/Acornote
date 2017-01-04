package tonnysunm.com.acornote.ui.folder;

import android.databinding.BaseObservable;
import android.view.View;

import tonnysunm.com.acornote.model.Item;

/**
 * Created by Tonny on 1/01/17.
 */

public class ItemViewModel extends BaseObservable {
    private Item mItem;

    public ItemViewModel(Item item) { this.mItem = item; }

    public void setItem(Item item) {
        this.mItem = item;
        notifyChange();
    }

    public String getTitle() {
        return mItem.title;
    }

    public String getImageUrl() { return  mItem.imgUrl; }

    public int getLinkVisibility() {
        return mItem.hasUrl() ? View.VISIBLE : View.GONE;
    }

    public int getImageVisibility() { return mItem.hasImage() ? View.VISIBLE : View.GONE;}

    public boolean hasImage() { return mItem.hasImage(); }

}
