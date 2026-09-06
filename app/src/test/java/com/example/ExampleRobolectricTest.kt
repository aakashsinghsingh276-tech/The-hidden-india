package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.util.DigipinUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Hidden Indian", appName)
    }

    @Test
    fun `verify digipin generation`() {
        // Test Himachal Tirthan coordinates
        val digipin = DigipinUtil.encode(31.6366, 77.3456)
        assertTrue(digipin.isNotEmpty())
        assertTrue(digipin.contains("-"))
    }
}
