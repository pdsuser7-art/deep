package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.SalesRepresentative
import com.example.model.formatIndianCurrency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("CompVantage IN", appName)
  }

  @Test
  fun `verify indian rupee currency formatting`() {
    assertEquals("₹4,82,50,000", formatIndianCurrency(48250000L))
    assertEquals("₹18,45,200", formatIndianCurrency(1845200L))
    assertEquals("₹37,50,000", formatIndianCurrency(3750000L))
    assertEquals("₹3,41,000", formatIndianCurrency(341000L))
  }

  @Test
  fun `verify compensation and surplus rollover calculation`() {
    val rep = SalesRepresentative(
        id = "EMP-1021",
        name = "Vikram Malhotra",
        initials = "VM",
        role = "Sr. Enterprise Sales Mgr",
        region = "Mumbai",
        targetAmount = 5000000L,
        actualSales = 6200000L,
        status = "Approved"
    )

    // 62L on 50L target = 124%
    assertEquals(124.0, rep.achievementPercent(false), 0.01)

    // Surplus rolled = 12,00,000
    assertEquals(1200000L, rep.surplusAmount(autoForwardActive = true, hsnExclusionActive = false))

    // Tier 3: 5.5% Super Tier
    assertEquals("5.5% Super Tier", rep.tierLabel(false))

    // TDS 10%
    assertTrue(rep.calculateTds(false) > 0)
  }
}
