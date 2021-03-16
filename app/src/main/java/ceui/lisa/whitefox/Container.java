package ceui.lisa.whitefox;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;

public class Container {

    private OkHttpClient mOkHttpClient;

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                    new SharedPrefsCookiePersistor(App.context));
            mOkHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .build();
        }
        return mOkHttpClient;
    }

    private Container() {
    }

    private static final class Holder {
        private static final Container INSTANCE = new Container();
    }

    public static Container get() {
        return Holder.INSTANCE;
    }
}
