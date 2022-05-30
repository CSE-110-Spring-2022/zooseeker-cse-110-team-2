package com.team2.zooseeker.view;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.team2.zooseeker.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ReplanTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    @Test
    public void replanTest() {
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
                allOf(withId(R.id.exhibitModel), withText("Crocodiles"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        3),
                                0),
                        isDisplayed()));
        materialCheckBox4.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_btn), withText("Plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.setting_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.gps_switch), withText("Enable GPS"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        switch_.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.latitiude_input), withText("0.0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("32.745311"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.latitiude_input), withText("32.745311"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.latitiude_input), withText("32.745311"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.longitude_input), withText("0.0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.longitude_input), withText("0.0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("-117.16627"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.longitude_input), withText("-117.16627"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.longitude_input), withText("-117.16627"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText7.perform(pressImeActionButton());

        pressBack();

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.reload_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button2), withText("No"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.direction_item_text), withText("Continue on Hippo Trail 1100 ft towards Crocodiles"),
                        withParent(withParent(withId(R.id.direction_items))),
                        isDisplayed()));
        textView.check(matches(withText("Continue on Hippo Trail 1100 ft towards Crocodiles")));

        pressBack();

        ViewInteraction materialCheckBox5 = onView(
                allOf(withId(R.id.exhibitModel), withText("Bali Mynah"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        0),
                                0),
                        isDisplayed()));
        materialCheckBox5.perform(click());

        ViewInteraction materialCheckBox6 = onView(
                allOf(withId(R.id.exhibitModel), withText("Blue Capped Motmot"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        1),
                                0),
                        isDisplayed()));
        materialCheckBox6.perform(click());

        ViewInteraction materialCheckBox7 = onView(
                allOf(withId(R.id.exhibitModel), withText("Capuchin Monkeys"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        2),
                                0),
                        isDisplayed()));
        materialCheckBox7.perform(click());

        ViewInteraction materialCheckBox8 = onView(
                allOf(withId(R.id.exhibitModel), withText("Crocodiles"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.exhibit_items),
                                        3),
                                0),
                        isDisplayed()));
        materialCheckBox8.perform(click());
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
