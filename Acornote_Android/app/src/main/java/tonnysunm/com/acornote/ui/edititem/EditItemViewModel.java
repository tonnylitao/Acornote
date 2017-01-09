package tonnysunm.com.acornote.ui.edititem;

import android.content.Context;
import android.content.DialogInterface;
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
import tonnysunm.com.acornote.model.Item;

//TODO: partly notify

public class EditItemViewModel extends BaseObservable {

    public EditItemMVP.View view;

    /**** ****/
    @Bindable public String title;
    @Bindable public String des;

    @Bindable public String url;
    @Bindable public String imgUrl;
    public String img;

    public int headTitle = R.string.edit_item_headtitle_edit;
    private String colorName;

    @Bindable public boolean translated;

    public EditItemViewModel(Item item, String colorName) {
        if (item != null) {
            this.title = item.title;
            this.des = item.des;
            this.url = item.url;
        }else {
            headTitle = R.string.edit_item_headtitle_add;
        }

        this.colorName = colorName;
    }

    /***** Chain setter *****/

    public EditItemViewModel setView(EditItemMVP.View view) {
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

        final AlertDialog dialog = builder.setTitle("Enter Item URL")
                .setCancelable(false) //TODO
                .setView(editText)
                .setPositiveButton("Sure", (DialogInterface d, int whichButton) -> {
                    final String text = editText.getText().toString();
                    if (text.startsWith("http://") || text.startsWith("https://")) {
                        EditItemViewModel.this.url = text;
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

    public int getColor() {
        final Context context = AcornoteApplication.getContext();
        return Folder.colorByName(context, colorName);
    }

    public void editImage() {

    }

    public void translate() {

    }

    public void swithTitleDes() {
        final String title = this.title;
        this.title = des;
        this.des = title;

        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.des);
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
                + ", "+des
                + " )";
    }
}
