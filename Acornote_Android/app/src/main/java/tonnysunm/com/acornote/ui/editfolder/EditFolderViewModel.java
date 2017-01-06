package tonnysunm.com.acornote.ui.editfolder;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;

//TODO: partly notify

public class EditFolderViewModel extends BaseObservable {

    public EditFolderMVP.View view;

    private Context context;

    /**** ****/
    public String colorName;
    public String title;
    public boolean hasLink;
    public boolean hasTag;
    public boolean hasFlip;
    public boolean hasAudio;

    public EditFolderViewModel(Folder folder) {
        if (folder != null) {
            this.colorName = folder.color;
            this.title = folder.title;

            this.hasLink = folder.url != null;

//            this.hasTag = false;
//            this.hasFlip = false;
//            this.hasAudio = false;
        }else {
            this.colorName = "sky";
        }
    }

    /***** Chain setter *****/

    public EditFolderViewModel setContext(Context context) {
        this.context = context;
        return this;
    }

    public EditFolderViewModel setView(EditFolderMVP.View view) {
        this.view = view;
        return this;
    }

    /*** Getter Setter ***/

    public void setHasLink(boolean hasLink) {
        this.hasLink = hasLink;

        notifyChange();
    }

    public void setHasTag(boolean hasTag) {
        this.hasTag = hasTag;

        notifyChange();
    }

    public void setHasFlip(boolean hasFlip) {
        this.hasFlip = hasFlip;

        notifyChange();
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;

        notifyChange();
    }

    public boolean isColorSelected(int index) {
        if (colorName != null) {
            final Resources res = context.getResources();
            String[] colors = res.getStringArray(R.array.folderColorKeys);
            if (index < colors.length) {
                return colorName.equals(colors[index]);
            }
        }

        return false;
    }

    public void selectColor(int index){
        final Resources res = context.getResources();
        final String[] colors = res.getStringArray(R.array.folderColorKeys);
        if (index >= colors.length) {
            return;
        }

        final String color = colors[index];
        if (!colorName.equals(color)) {
            colorName = color;
            notifyChange();
        }
    }

    public int getColor() {
        return Folder.colorByName(context, colorName);
    }

    @BindingAdapter("layout_marginBottom")
    public static void setLayoutMarginBottom(View view, float margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin, (int)margin);
        view.setLayoutParams(layoutParams);
    }

}
