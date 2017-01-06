package tonnysunm.com.acornote.ui.editfolder;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.model.Folder;

public class EditFolderViewModel extends BaseObservable {
    private Folder mFolder;

    private Context mContext;

    private EditFolderMVP.View mMVPView;

    public EditFolderMVP.View getMVPView() {
        return mMVPView;
    }

    public void setMVPView(EditFolderMVP.View MVPView) {
        mMVPView = MVPView;
    }

    public int getColor() {
        if (mFolder == null) {
            return ContextCompat.getColor(mContext, R.color.blue);
        }

        return ContextCompat.getColor(mContext, R.color.sky);
    }

    public EditFolderViewModel(Context context, Folder folder) {
        mContext = context;
        mFolder = folder;

        notifyChange();
    }

    public int getId() {
        return  mFolder.id;
    }

    public void setFolder(Folder folder) {
        this.mFolder = folder;
    }

    public String getTitle() {
        return mFolder.title;
    }

    @BindingAdapter("layout_marginBottom")
    public static void setLayoutMarginBottom(View view, float margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin, (int)margin);
        view.setLayoutParams(layoutParams);
    }
}
