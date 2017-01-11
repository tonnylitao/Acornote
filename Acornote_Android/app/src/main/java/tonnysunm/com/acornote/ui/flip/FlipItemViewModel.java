package tonnysunm.com.acornote.ui.flip;

import android.animation.AnimatorSet;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import tonnysunm.com.acornote.AcornoteApplication;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.model.Item;

/**
 * Created by Tonny on 1/01/17.
 */

public class FlipItemViewModel extends BaseObservable {
    public Item item;
    public String colorName;

    public FlipMVP.View view;

    public int flipable;

    public AnimatorSet flipIn;
    public AnimatorSet flipOut;

    @Bindable public int backVisibility = View.GONE;

    public FlipItemViewModel(Item item) {
        this.item = item;
    }

    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }

    public int getFlipable() {
        return flipable;
    }

    public boolean isMarked() {
        return item.isMarked();
    }

    public String getTitle() {
        return item.title;
    }

    public String getImageUrl() { return  item.imgUrl; }

    public int getLinkVisibility() {
        return item.hasUrl() ? View.VISIBLE : View.GONE;
    }

    public int getImageVisibility() { return item.hasImage() ? View.VISIBLE : View.GONE;}

    public boolean hasImage() { return item.hasImage(); }

    public int getColor() {
        final Context context = AcornoteApplication.getContext();
        return Folder.colorByName(context, colorName);
    }

    public void flip(View view) {
        View card = (View) view.getParent().getParent();
        card.animate()
                .rotationY(100)
                .setDuration(3000)
                .start();

//        flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(view.getContext(), R.animator.flip_out_animation);
//        flipOut.setTarget(card);
//        flipOut.start();
    }

}
