package ceui.lisa.whitefox.ui.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.MainActivity
import ceui.lisa.whitefox.Params
import ceui.lisa.whitefox.models.User
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import rxhttp.RxHttp

class LoginViewModel: ViewModel() {

    var userJson: MutableLiveData<String> = MutableLiveData()

    fun login(name: String, pwd: String) {
        RxHttp.get("http://192.243.123.124:3000/login/cellphone?phone=" +
                name + "&password=" + pwd)
                .asString()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe {
                    userJson.value = it
                }
    }
}