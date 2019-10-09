package fr.unice.i3s.uca4svr.toucan_vr.dashSRD;

import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;

public interface MPDEventListener extends AdaptiveMediaSourceEventListener {
    void onManifestLoaded(DashManifest manifest);
}
