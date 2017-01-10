package tonnysunm.com.acornote.ui.flip;

import android.util.Log;

import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.base.BasePresenter;

/**
 * Created by Tonny on 20/12/16.
 */

class FlipPresenter extends BasePresenter<FlipMVP.View> implements FlipMVP.Presenter {
    private static final String TAG = FlipPresenter.class.getSimpleName();

    @Override
    public void loadData(int folderId){
        Log.d(TAG, "loadData");
        if (folderId == 0) {
            Item.findAllAsync(getMVPView()::refresh);
        }else {
            Item.findAllInFolderAsync(folderId, getMVPView()::refresh);
        }
    }

}