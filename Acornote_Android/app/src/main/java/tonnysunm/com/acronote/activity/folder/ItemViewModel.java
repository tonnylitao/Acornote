package tonnysunm.com.acronote.activity.folder;

import tonnysunm.com.acronote.model.Item;

/**
 * Created by Tonny on 1/01/17.
 */

public class ItemViewModel {
    Item item;

    public String title;

    public ItemViewModel(Item item) {
        this.item = item;
    }

    public String getTitle() {
        return item.title;
    }

    public boolean hasImage() { return item.hasImage(); }

    public boolean hasUrl() {
        return item.hasUrl();
    }
}
