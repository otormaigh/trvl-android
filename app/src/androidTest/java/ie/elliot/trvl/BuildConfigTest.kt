package ie.elliot.trvl

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import ie.elliot.trvl.ui.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BuildConfigTest {

    @Test
    fun testApplicationId() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        if (BuildConfig.BUILD_TYPE == "debug") {
            assertEquals("ie.elliot.trvl.debug", appContext.packageName)
        } else if (BuildConfig.BUILD_TYPE == "qa") {
            assertEquals("ie.elliot.trvl.qa", appContext.packageName)
        } else if (BuildConfig.BUILD_TYPE == "release") {
            assertEquals("ie.elliot.trvl", appContext.packageName)
        }
    }
}
