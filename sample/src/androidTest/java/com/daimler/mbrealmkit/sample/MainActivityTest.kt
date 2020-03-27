package com.daimler.mbrealmkit.sample

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val mainActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java,
            true,
            true
    )

    /**
     * Check whether text in the name field is properly displayed.
     */
    @Test
    fun testTextView() {
        mainActivityTestRule.launchActivity(Intent())
        val testName = "testName"
        onView(withId(R.id.edit_name)).perform(typeText("testName"))
        onView(withId(R.id.edit_name)).check(matches(withText(testName)))
    }
}
