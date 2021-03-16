package ceui.lisa.whitefox.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class VH<Layout extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected Layout baseBind;

    public VH(@NonNull View itemView) {
        super(itemView);
        baseBind = DataBindingUtil.bind(itemView);
    }
}
