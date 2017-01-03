package tonnysunm.com.acronote;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tonnysunm.com.acronote.model.Folder;

/**
 * Created by Tonny on 1/01/17.
 */

public class AcronoteApplication extends Application {
    public static Realm REALM;

    private static WeakReference<Context> mContext;

    public static Context getContext() {
        return mContext.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        AcronoteApplication.mContext = new WeakReference<Context>(this.getApplicationContext());

        if (AcronoteApplication.REALM == null) {
            final RealmConfiguration config = new RealmConfiguration.Builder().initialData(Folder::createInitialData).build();

            if (BuildConfig.DEBUG) {
                Realm.deleteRealm(config);
            }
            AcronoteApplication.REALM = Realm.getInstance(config);
        }
    }
}

