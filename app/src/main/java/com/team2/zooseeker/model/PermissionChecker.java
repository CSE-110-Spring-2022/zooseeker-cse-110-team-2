package com.team2.zooseeker.model;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class PermissionChecker {
    private final ComponentActivity activity;
    final ActivityResultLauncher<String[]> requestPermissionLauncher;

    public PermissionChecker(ComponentActivity activity) {
        this.activity = activity;
        requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
            perms.forEach((perm, isGranted) -> {
                Log.i("DEBUG", String.format("Permission %s granted: %s", perm, isGranted));
            });
        });
    }

    /**
     * Checks to make sure all required permissions exist
     * @return true if permissions have to be requested, false if permissions already exist
     */
    public boolean ensurePermissions() {
        String[] requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean hasNoLocationPerms = Arrays.stream(requiredPermissions)
                .map(perm -> ContextCompat.checkSelfPermission(activity, perm))
                .allMatch(status -> status == PackageManager.PERMISSION_DENIED);

        if (hasNoLocationPerms) {
            requestPermissionLauncher.launch(requiredPermissions);
            return true;
        }
        return false;
    }

    /**
     * Used to check permissions after launching
     * @return true if permissions exist, false otherwise
     */
    public static boolean hasPermissions(Activity activity) {
        String[] requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean hasNoLocationPerms = Arrays.stream(requiredPermissions)
                .map(perm -> ContextCompat.checkSelfPermission(activity, perm))
                .allMatch(status -> status == PackageManager.PERMISSION_DENIED);

        return !hasNoLocationPerms;
    }
}
