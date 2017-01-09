package tonnysunm.com.acornote.ui.flip;

import android.util.Log;

import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.base.BasePresenter;

/**
 * Created by Tonny on 20/12/16.
 */

class FlipPresenter extends BasePresenter<FlipMVP.View> implements FlipMVP.Presenter {
    private static final String TAG = FlipPresenter.class.getSimpleName();

    @Override
    public void loadData(){
        Log.d(TAG, "loadData");
        Folder.findAllAsync(result -> {
            getMVPView().refresh(result);
        });
    }

}