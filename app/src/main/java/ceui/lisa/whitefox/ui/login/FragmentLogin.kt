package ceui.lisa.whitefox.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.MainActivity
import ceui.lisa.whitefox.Params
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.FragmentLoginBinding
import ceui.lisa.whitefox.models.User
import ceui.lisa.whitefox.ui.base.BaseFragment
import com.google.gson.Gson
import com.hjq.toast.ToastUtils

class FragmentLogin: BaseFragment<FragmentLoginBinding>() {

    private lateinit var loginViewModel: LoginViewModel

    override fun layout(): Int {
        return R.layout.fragment_login
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.userJson.observe(this, object : Observer<String> {
            override fun onChanged(userJson: String) {
                if (!TextUtils.isEmpty(userJson)) {
                    Log.d("userJson", userJson)
                    //把登录返回的数据存储到mmkv
                    App.mmkv.encode(Params.USER_JSON, userJson)
                    //更新user对象
                    App.user = Gson().fromJson(userJson, User::class.java)

                    //跳转到主页面
                    ToastUtils.show("登录成功！")
                    val intent = Intent(mContext, MainActivity::class.java)
                    startActivity(intent)
                    mActivity.finish()
                }
            }
        })
    }

    override fun initView() {
        baseBind.login.setOnClickListener{
            tryLogin()
        }
    }

    private fun tryLogin() {
        val name = baseBind.phone.text.toString()
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show("请输入手机号")
            return
        }

        val pwd = baseBind.pwd.text.toString()
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show("请输入密码")
            return
        }

        loginViewModel.login(name, pwd)
    }
}