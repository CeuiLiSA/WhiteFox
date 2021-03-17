package ceui.lisa.whitefox.test;

import androidx.annotation.NonNull;

import ceui.lisa.whitefox.models.Song;

public abstract class OnPlayListener {

    public abstract void onPrepared(@NonNull Song song);

    public abstract void beforePrepared();
}
