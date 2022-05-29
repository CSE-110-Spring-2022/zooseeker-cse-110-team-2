package com.team2.zooseeker.viewModel;

public class MockLocationStore {

    private static MockLocationStore instance;

    private double longitude, latitude;

    private MockLocationStore() {
        longitude = 0;
        latitude = 0;
    }

    public synchronized static MockLocationStore getSingleton() {
        if (instance == null) {
            instance = new MockLocationStore();
        }
        return instance;
    }


    public void setLocation(double lat, double lng) {
        this.longitude = lng;
        this.latitude = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
