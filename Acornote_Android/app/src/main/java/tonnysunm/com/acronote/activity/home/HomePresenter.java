package tonnysunm.com.acronote.activity.home;

import tonnysunm.com.acronote.model.Folder;
import tonnysunm.com.acronote.mvp.BasePresenter;

/**
 * Created by Tonny on 20/12/16.
 */

class HomePresenter extends BasePresenter<HomeMVP.View> implements HomeMVP.Presenter {
    private static final String TAG = HomePresenter.class.getSimpleName();

    @Override
    public void loadData(){
        Folder.findAllAsync(result -> {
            mView.refresh(result);
        });
    }

}