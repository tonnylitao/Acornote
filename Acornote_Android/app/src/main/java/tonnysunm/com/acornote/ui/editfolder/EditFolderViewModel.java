package tonnysunm.com.acornote.ui.editfolder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import tonnysunm.com.acornote.BR;
import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;

//TODO: partly notify

public class EditFolderViewModel extends BaseObservable {

    public EditFolderMVP.View view;

    private Context context;

    /**** ****/
    public String colorName;
    public String title;
    @Bindable public String url;

    @Bindable public boolean hasTag;
    @Bindable public boolean hasFlip;
    @Bindable public boolean hasAudio;

    public EditFolderViewModel(Folder folder) {
        if (folder != null) {
            this.colorName = folder.colorName;
            this.title = folder.title;
            this.url = folder.url;

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

    public void editUrl() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final AppCompatEditText editText = new AppCompatEditText(context);
        editText.setHint("https://");
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        final AlertDialog dialog = builder.setTitle("Enter Folder URL")
                .setCancelable(false) //TODO
                .setView(editText)
                .setPositiveButton("Sure", (DialogInterface d, int whichButton) -> {
                    final String text = editText.getText().toString();
                    if (text.startsWith("http://") || text.startsWith("https://")) {
                        EditFolderViewModel.this.url = text;
                        notifyPropertyChanged(BR.url);
                        d.dismiss();
                    }else{
                        Toast.makeText(context, "Need http or https url", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setHasTag(boolean hasTag) {
        this.hasTag = hasTag;

        notifyPropertyChanged(BR.hasTag);
    }

    public void setHasFlip(boolean hasFlip) {
        this.hasFlip = hasFlip;

        notifyPropertyChanged(BR.hasFlip);
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;

        notifyPropertyChanged(BR.hasAudio);
    }

    public boolean isSelected(int index) {
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

    @Override
    public String toString() {
        return "( " +colorName + ", " + title
                + ", "+url
                + ", "+hasTag
                + ", "+hasFlip
                + ", "+hasAudio
                + " )";
    }
}
