package ceui.lisa.whitefox.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<Layout : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var baseBind: Layout
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        baseBind = DataBindingUtil.setContentView(this, layout())
        mContext = this
        initView()
        initData()
    }

    abstract fun layout(): Int
    open fun initView() {}
    fun initData() {}
    open fun beforeSetContentView() {}
}