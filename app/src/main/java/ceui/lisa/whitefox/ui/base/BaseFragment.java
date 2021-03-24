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

import java.util.UUID;


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
        Log.d("method trace" + className, "construct" + uuid);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mContext = requireContext();
            mActivity = requireActivity();

            Log.d("method trace" + className, "onCreate");
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
            rootView = inflater.inflate(layout(), container, false);
            baseBind = DataBindingUtil.bind(rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            initModel();
            onViewModelCreated();

            initView();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initModel() {

    }

    public abstract int layout();

    public void bundle(Bundle bundle) {

    }

    public abstract void initView();

    public void initData() {

    }

    public void onViewModelCreated() {

    }
}
