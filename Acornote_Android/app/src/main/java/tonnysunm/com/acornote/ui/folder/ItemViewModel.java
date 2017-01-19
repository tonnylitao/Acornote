package tonnysunm.com.acornote.ui.folder;

import android.databinding.BaseObservable;
import android.view.View;

import tonnysunm.com.acornote.model.Item;

/**
 * Created by Tonny on 1/01/17.
 */

public class ItemViewModel extends BaseObservable {
    public Item item;

    public ItemViewModel(Item item) { this.item = item; }

    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }

    public String getImageUrl() { return  item.imgUrl; }

    public int getLinkVisibility() {
        return item.hasUrl() ? View.VISIBLE : View.GONE;
    }

    public int getImageVisibility() { return item.hasImage() ? View.VISIBLE : View.GONE;}

    public boolean hasImage() { return item.hasImage(); }

}
