package tonnysunm.com.acornote.ui.flip;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import tonnysunm.com.acornote.AcornoteApplication;
import tonnysunm.com.acornote.R;
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
    public AnimatorSet flipAnimation;

    @Bindable public int backVisibility = View.GONE;

    public FlipItemViewModel(Item item) {
        this.item = item;
        if (item.des != null || item.imgUrl != null) {
            backVisibility = View.VISIBLE;
        }

        flipAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(AcornoteApplication.getContext(), R.animator.flip_animation);
        showAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(AcornoteApplication.getContext(), R.animator.flip_show_animation);
        hideAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(AcornoteApplication.getContext(), R.animator.flip_hide_animation);
    }

    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }

    public int getFlipable() {
        return backVisibility == View.GONE ? View.GONE : View.VISIBLE;
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

    private AnimatorSet showAnimation;
    private AnimatorSet hideAnimation;

    public void flip(View view) {
        assert backVisibility != View.GONE;

        View card = (View) view.getParent();

        View titleView = card.findViewById(R.id.front_txtview);
        View scrollView = card.findViewById(R.id.back_scrollview);

        flipAnimation.cancel();
        showAnimation.cancel();
        hideAnimation.cancel();

        if (scrollView.getAlpha() == 0) {
            flipAnimation.setTarget(card);
            flipAnimation.start();

            showAnimation.setTarget(scrollView);
            showAnimation.start();

            hideAnimation.setTarget(titleView);
            hideAnimation.start();
        }else {
            flipAnimation.setTarget(card);
            flipAnimation.start();

            showAnimation.setTarget(titleView);
            showAnimation.start();

            hideAnimation.setTarget(scrollView);
            hideAnimation.start();
        }
    }

}
