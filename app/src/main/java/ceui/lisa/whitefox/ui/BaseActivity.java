package ceui.lisa.whitefox.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<Layout extends ViewDataBinding> extends AppCompatActivity {

    protected Layout baseBind;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        baseBind = DataBindingUtil.setContentView(this, layout());
        mContext = this;
        initView();
        initData();
    }

    public abstract int layout();

    public void initView(){

    }

    public void initData(){

    }

    public void beforeSetContentView(){

    }
}
