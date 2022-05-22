package com.team2.zooseeker;

import static junit.framework.TestCase.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.ReplanModel;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReplanTest {
    @Test
    public void testDistToEdge() {
        assertEquals(1.0, ReplanModel.distToEdge(0, 0, 5, 0, 2, 1));
        assertEquals(1.0, ReplanModel.distToEdge(0, 0, 5, 0, 2, -1));
        assertEquals(0.0, ReplanModel.distToEdge(0, 0, 5, 5, 3, 3));
        assertEquals((float) Math.sqrt(2) / 2, (float) ReplanModel.distToEdge(0, 0, 5, 5, 1, 0));
        assertEquals((float) (2.5 * Math.sqrt(2)), (float) ReplanModel.distToEdge(0, 0, 5, 5, 0, 5));
    }

    @Test
    public void testDistToNearestLandmark() {
        //TODO: write test
    }
}
