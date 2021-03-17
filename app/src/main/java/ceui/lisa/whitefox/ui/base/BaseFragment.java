package ceui.lisa.whitefox.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

import ceui.lisa.whitefox.test.MessageEvent;


public abstract class BaseFragment<Layout extends ViewDataBinding> extends Fragment {

    protected View rootView;
    protected Context mContext;
    protected FragmentActivity mActivity;
    protected Layout baseBind;
    protected String className = getClass().getSimpleName() + " ";
    protected String uuid;

    public BaseFragment() {
        uuid = UUID.randomUUID().toString();
        Log.d(className, "newInstance " + uuid);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mContext = requireContext();
            mActivity = requireActivity();

            Bundle bundle = getArguments();
            if (bundle != null) {
                bundle(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        try {
            if (rootView == null || baseBind == null) {
                rootView = inflater.inflate(layout(), container, false);
                baseBind = DataBindingUtil.bind(rootView);
                initView();
                initData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    public abstract int layout();

    public void bundle(Bundle bundle) {

    }

    public abstract void initView();

    public void initData() {

    }
}
