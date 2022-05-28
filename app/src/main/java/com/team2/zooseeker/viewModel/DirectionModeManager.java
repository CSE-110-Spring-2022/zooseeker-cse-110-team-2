package com.team2.zooseeker.viewModel;

public class DirectionModeManager {

    private static DirectionModeManager instance;

    private boolean inDetailedDirectionMode;

    private DirectionModeManager() {
        inDetailedDirectionMode = true;
    }


    public synchronized static DirectionModeManager getSingleton() {
        if (instance == null) {
            instance = new DirectionModeManager();
        }
        return instance;
    }

    public boolean getIsInDetailedMode() {
        return inDetailedDirectionMode;
    }

    public boolean getIsInBriefMode() {
        return !inDetailedDirectionMode;
    }

    public void setDetailedMode() {
        inDetailedDirectionMode = true;
    }

    public void setBriefMode() {
        inDetailedDirectionMode = false;
    }
}
