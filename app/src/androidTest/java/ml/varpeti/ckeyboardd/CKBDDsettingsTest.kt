package ml.varpeti.ckeyboardd


import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CKBDDsettingsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(CKBDDsettings::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Test
    fun cKBDDsettingsTest()
    {
        // Write by my hand...

        ////////////////////// New button: banana
        val settingsKeys = onView(allOf(withId(R.id.settings_keys)))
        settingsKeys.perform(click())

        val new = onView(allOf(withText("NEW")))
        new.perform(scrollTo(), click())

        val tid = onView(allOf(withId(R.id.tid)))
        tid.perform(scrollTo(), ViewActions.replaceText("banana"))

        val tshowprimary = onView(allOf(withId(R.id.tshowprimary)))
        tshowprimary.perform(scrollTo(), ViewActions.replaceText("\uD83C\uDF4C"))

        val tcmdnormal = onView(allOf(withId(R.id.tcmdnormal)))
        tcmdnormal.perform(scrollTo(), ViewActions.replaceText("0\n  print\n    \uD83C\uDF4C"))

        val bsave = onView(allOf(withId(R.id.bsave)))
        bsave.perform(scrollTo(), click())

        val bananaPrimary = onView(allOf(withId(R.id.primary), withText("\uD83C\uDF4C")))
        bananaPrimary.perform(scrollTo())

        Thread.sleep(1000)

        bananaPrimary.check(matches(isDisplayed()))

        Espresso.pressBack()

        ////////////////////// Replace in the "capslock:num" row the "capslock:0" button with "banana"
        val settingsRows = onView(allOf(withId(R.id.settings_rows)))
        settingsRows.perform(click())

        val cpnum = onView(allOf(withText("capslock:num")))
        cpnum.perform(scrollTo(), click())

        val cp0 = onView(allOf(withText("capslock:0")))
        cp0.perform(scrollTo(), ViewActions.replaceText("banana"))

        val bsave2 = onView(allOf(withId(R.id.bsave)))
        bsave2.perform(scrollTo(), click())

        val bananaPrimary2 = onView(allOf(withId(R.id.primary), withText("\uD83C\uDF4C")))
        bananaPrimary2.perform(scrollTo())

        Thread.sleep(1000)

        bananaPrimary2.check(matches(isDisplayed()))

        Espresso.pressBack()

        ////////////////////// Check in the capslock keyboard too
        val settingsKeyboards = onView(allOf(withId(R.id.settings_keyboards)))
        settingsKeyboards.perform(click())

        val bananaPrimary3 = onView(allOf(withId(R.id.primary), withText("\uD83C\uDF4C")))
        bananaPrimary3.perform(scrollTo())

        Thread.sleep(1000)

        bananaPrimary3.check(matches(isDisplayed()))

        Espresso.pressBack()

        ////////////////////// Reset
        val rds = onView(allOf(withId(R.id.reset_default_settings)))
        rds.perform(longClick())

        settingsKeyboards.perform(click())

        // Bet 10$ it can be done better
        var b = false
        try
        {
            val nobanana = onView(allOf(withId(R.id.primary), withText("\uD83C\uDF4C")))
        }
        catch (ex : NoMatchingViewException)
        {
            b = true
        }

        if (!b) assert(false) { "banana is still in the View Hierarchy!" }


    }
}
