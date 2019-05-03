package com.nilhcem.blenamebadge.ui.custom.knob;

public interface OnCrollerChangeListener {
    void onProgressChanged(Croller croller, int progress);

    void onStartTrackingTouch(Croller croller);

    void onStopTrackingTouch(Croller croller);
}
