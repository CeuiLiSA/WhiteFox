package ceui.lisa.whitefox.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import ceui.lisa.whitefox.R
import com.blankj.utilcode.util.BarUtils

abstract class BaseActivity<Layout : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var baseBind: Layout
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BarUtils.setNavBarColor(this, getColor(R.color.player_bg))
        BarUtils.setStatusBarColor(this, getColor(R.color.player_bg))

        baseBind = DataBindingUtil.setContentView(this, layout())
        mContext = this
        initView()
        initData()
    }

    abstract fun layout(): Int
    open fun initView() {}
    fun initData() {}
}