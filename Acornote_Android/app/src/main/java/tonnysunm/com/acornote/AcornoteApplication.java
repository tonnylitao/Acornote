package tonnysunm.com.acornote;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tonnysunm.com.acornote.model.Folder;

/**
 * Created by Tonny on 1/01/17.
 */

public class AcornoteApplication extends Application {
    public static Realm REALM;

    private static WeakReference<Context> mContext;

    public static Context getContext() {
        return mContext.get();
    }

    public static AcornoteApplication get(Context context) {
        return (AcornoteApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        AcornoteApplication.mContext = new WeakReference<Context>(this.getApplicationContext());

        if (AcornoteApplication.REALM == null) {
            final RealmConfiguration config = new RealmConfiguration.Builder().initialData(Folder::createInitialData).build();

            if (BuildConfig.DEBUG) {
                Realm.deleteRealm(config);
            }
            AcornoteApplication.REALM = Realm.getInstance(config);
        }
    }
}

