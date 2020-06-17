package com.example.weihnachtmaerkte

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.weihnachtmaerkte.markets.detailedview.DetailedMarketActivity
import org.junit.Test

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DetailedMarketRateFragmentTest {

    val ITEM_ID_IN_TEST = "0"
    val MARKETS = FakeMarketsData.markets
    val RATINGS = FakeMarketsData.ratings

    val activityRule = ActivityTestRule(DetailedMarketActivity::class.java, true, false)

    @Test(expected = NoActivityResumedException::class)
    fun clearBackStack_GivenDetailedMarketActivity_ExpectedNoActivityResumedException() {
        val bundle = Bundle()
        bundle.putString("id", ITEM_ID_IN_TEST)
        bundle.putParcelableArrayList("markets", MARKETS)
        bundle.putParcelableArrayList("ratings", RATINGS)

        val intent = Intent()
        intent.putExtra("bundle",bundle)
        activityRule.launchActivity(intent)

        onView(withId(R.id.layout_detailed_market_activity)).check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.button_more_comments)).perform(click())
        onView(withId(R.id.comments_go_back)).perform(click())
        onView(withId(R.id.fab_detailed_edit)).perform(click())
        onView(withId(R.id.button_rate_go_back)).perform(click())
        pressBack()
    }
}