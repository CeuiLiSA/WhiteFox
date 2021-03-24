package ceui.lisa.whitefox.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BindFragmentList<Layout extends ViewDataBinding, Bean>
        extends FragmentList<Bean> {

    protected Layout baseBind;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            baseBind = DataBindingUtil.inflate(inflater, layout(), container, false);
            return baseBind.getRoot();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public abstract int layout();
}
