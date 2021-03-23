package ceui.lisa.whitefox.core;

import android.view.View;

public interface OnCheckChangeListener {

    void onSelect(int index, View view);

    void onReselect(int index, View view);
}
