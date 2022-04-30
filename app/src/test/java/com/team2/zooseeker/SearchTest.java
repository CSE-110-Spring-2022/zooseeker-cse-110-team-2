package com.team2.zooseeker;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.SearchModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Test
    public void searchFunctionTest(){
        ArrayList<String> mockData = new ArrayList<String>();
        mockData.add("bear");
        mockData.add("tiger");
        mockData.add("elephant");
        mockData.add("bird");
        mockData.add("bee");
        mockData.add("monkey");
        mockData.add("gorilla");
        mockData.add("lion");
        mockData.add("zebra");
        SearchModel model = new SearchModel(mockData);
        List<String> output;
        output = model.search("li", 2);
        assertEquals("lion", output.get(0));
        output = model.search("be", 2);
        assertEquals("bear", output.get(0));
        assertEquals("bee", output.get(1));

    }
}
