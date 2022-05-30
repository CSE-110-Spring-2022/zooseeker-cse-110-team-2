package com.team2.zooseeker.view;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.team2.zooseeker.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ShowAllSelectedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void showAllSelectedTest() {
        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.exhibitModel), withText("Bali Mynah"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        0),
                                0),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.exhibitModel), withText("Blue Capped Motmot"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        1),
                                0),
                        isDisplayed()));
        materialCheckBox2.perform(click());

        ViewInteraction materialCheckBox3 = onView(
                allOf(withId(R.id.exhibitModel), withText("Capuchin Monkeys"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        2),
                                0),
                        isDisplayed()));
        materialCheckBox3.perform(click());

        ViewInteraction materialCheckBox4 = onView(
                allOf(withId(R.id.exhibitModel), withText("Fern Canyon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        5),
                                0),
                        isDisplayed()));
        materialCheckBox4.perform(click());

        ViewInteraction materialCheckBox5 = onView(
                allOf(withId(R.id.Show_selected), withText("Show All Selected Exhibits"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialCheckBox5.perform(click());

        ViewInteraction checkBox = onView(
                allOf(withId(R.id.exhibitModel), withText("Fern Canyon"),
                        withParent(withParent(withId(R.id.exhibit_items))),
                        isDisplayed()));
        checkBox.check(matches(isDisplayed()));

        ViewInteraction materialCheckBox6 = onView(
                allOf(withId(R.id.Show_selected), withText("Show All Selected Exhibits"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialCheckBox6.perform(click());

        ViewInteraction materialCheckBox7 = onView(
                allOf(withId(R.id.exhibitModel), withText("Fern Canyon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        5),
                                0),
                        isDisplayed()));
        materialCheckBox7.perform(click());

        ViewInteraction materialCheckBox8 = onView(
                allOf(withId(R.id.exhibitModel), withText("Capuchin Monkeys"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        2),
                                0),
                        isDisplayed()));
        materialCheckBox8.perform(click());

        ViewInteraction materialCheckBox9 = onView(
                allOf(withId(R.id.exhibitModel), withText("Blue Capped Motmot"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        1),
                                0),
                        isDisplayed()));
        materialCheckBox9.perform(click());

        ViewInteraction materialCheckBox10 = onView(
                allOf(withId(R.id.exhibitModel), withText("Bali Mynah"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        0),
                                0),
                        isDisplayed()));
        materialCheckBox10.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
