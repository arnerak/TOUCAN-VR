package fr.unice.i3s.uca4svr.toucan_vr.dashSRD.manifest;

import android.net.Uri;

import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.UtcTimingElement;

import java.util.List;

import fr.unice.i3s.uca4svr.toucan_vr.dynamicEditing.DynamicEditingHolder;

public class DashManifestExtended extends DashManifest {

    DynamicEditingHolder dynamicEditingHolder;

    public DashManifestExtended(long availabilityStartTime, long duration, long minBufferTime, boolean dynamic,
                                long minUpdatePeriod, long timeShiftBufferDepth, long suggestedPresentationDelay,
                                UtcTimingElement utcTiming, Uri location, List<Period> periods, DynamicEditingHolder dynamicEditingHolder) {
        super(availabilityStartTime, duration, minBufferTime, dynamic, minUpdatePeriod, timeShiftBufferDepth, suggestedPresentationDelay, utcTiming, location, periods);
        this.dynamicEditingHolder = dynamicEditingHolder;
    }

    public DynamicEditingHolder getDynamicEditingHolder() {
        return dynamicEditingHolder;
    }
}
