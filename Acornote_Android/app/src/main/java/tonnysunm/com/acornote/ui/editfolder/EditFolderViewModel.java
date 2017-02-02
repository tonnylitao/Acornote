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

import tonnysunm.com.acornote.AcornoteApplication;
import tonnysunm.com.acornote.BR;
import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;

public class EditFolderViewModel extends BaseObservable {

    public EditFolderMVP.View view;

    /**** ****/
    public String colorName;
    public String title;
    @Bindable public String url;

    @Bindable public boolean markable;
    @Bindable public boolean flashcardable;
    @Bindable public boolean audioPlayable;

    public EditFolderViewModel(Folder folder) {
        if (folder != null) {
            this.colorName = folder.colorName;
            this.title = folder.title;
            this.url = folder.url;

            this.markable = folder.markable;
            this.flashcardable = folder.flashcardable;
            this.audioPlayable = folder.audioPlayable;
        }else {
            this.colorName = AcornoteApplication.getContext().getString(R.string.defaultColor);
        }
    }

    /***** Chain setter *****/

    public EditFolderViewModel setView(EditFolderMVP.View view) {
        this.view = view;
        return this;
    }

    /*** Getter Setter ***/

    public void editUrl(View view) {
        final Context context = view.getContext();
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

    public void setMarkable(boolean markable) {
        this.markable = markable;

        notifyPropertyChanged(BR.markable);
    }

    public void setFlashcardable(boolean flashcardable) {
        this.flashcardable = flashcardable;

        notifyPropertyChanged(BR.flashcardable);
    }

    public void setAudioPlayable(boolean audioPlayable) {
        this.audioPlayable = audioPlayable;

        notifyPropertyChanged(BR.audioPlayable);
    }

    public boolean isSelected(int index) {
        if (colorName != null) {
            final Context context = AcornoteApplication.getContext();
            final Resources res = context.getResources();
            String[] colors = res.getStringArray(R.array.folderColorKeys);
            if (index < colors.length) {
                return colorName.equals(colors[index]);
            }
        }

        return false;
    }

    public void selectColor(int index){
        final Context context = AcornoteApplication.getContext();
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
        final Context context = AcornoteApplication.getContext();
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
                + ", "+ markable
                + ", "+ flashcardable
                + ", "+ audioPlayable
                + " )";
    }
}
