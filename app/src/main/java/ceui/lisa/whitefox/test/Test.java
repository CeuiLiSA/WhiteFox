package ceui.lisa.whitefox.test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public abstract class Test<T> {

    {
        List<String> temp = new ArrayList<>();
    }

    abstract Class<? extends ListViewModel<?>> modelClass();

}
