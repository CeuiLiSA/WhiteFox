package ceui.lisa.whitefox

import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient

object Container {

    var client: OkHttpClient

    init {
        val cookieJar: ClearableCookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(App.context)
        )
        client = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build()
    }
}