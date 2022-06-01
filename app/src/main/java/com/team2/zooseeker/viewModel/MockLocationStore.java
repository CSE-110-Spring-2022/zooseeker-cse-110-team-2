package com.team2.zooseeker.viewModel;

import android.location.Location;

public class MockLocationStore extends Location {

    private static MockLocationStore instance;

    private boolean enabled;

    private MockLocationStore() {
        super("");
    }

    public synchronized static MockLocationStore getSingleton() {
        if (instance == null) {
            instance = new MockLocationStore();
        }
        return instance;
    }


    public void setLocation(double lat, double lng) {
        super.setLatitude(lat);
        super.setLongitude(lng);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getLongitude() {
        return super.getLongitude();
    }

    public double getLatitude() {
        return super.getLatitude();
    }

    public boolean gpsEnabled() { return enabled; }
}
