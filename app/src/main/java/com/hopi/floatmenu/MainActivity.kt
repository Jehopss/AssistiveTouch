package com.hopi.floatmenu

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.color.DynamicColors

/** Onboarding for now; becomes the settings screen later. */
class MainActivity : AppCompatActivity() {

    private lateinit var statusTitle: TextView
    private lateinit var statusBody: TextView
    private lateinit var openSettingsButton: Button
    private lateinit var troubleshootText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        statusTitle = findViewById(R.id.status_title)
        statusBody = findViewById(R.id.status_body)
        openSettingsButton = findViewById(R.id.open_settings)
        troubleshootText = findViewById(R.id.troubleshoot)

        openSettingsButton.setOnClickListener { openAccessibilitySettings() }
    }

    override fun onResume() {
        super.onResume()
        // Re-check every time we come back from the Settings app.
        renderStatus(isServiceEnabled())
    }

    private fun renderStatus(enabled: Boolean) {
        if (enabled) {
            statusTitle.setText(R.string.status_on_title)
            statusTitle.setTextColor(getColorStateList(R.color.status_on))
            statusBody.setText(R.string.status_on_body)
            openSettingsButton.setText(R.string.button_manage)
        } else {
            statusTitle.setText(R.string.status_off_title)
            statusTitle.setTextColor(getColorStateList(R.color.status_off))
            statusBody.setText(R.string.status_off_body)
            openSettingsButton.setText(R.string.button_turn_on)
        }
        troubleshootText.isVisible = !enabled
    }

    private fun isServiceEnabled(): Boolean {
        val manager = getSystemService(AccessibilityManager::class.java)
        return manager
            .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any {
                val info = it.resolveInfo.serviceInfo
                info.packageName == packageName && info.name == FloatMenuService::class.java.name
            }
    }

    /** Jump straight to FloatMenu's own page; fall back to the main Accessibility list. */
    private fun openAccessibilitySettings() {
        val component = ComponentName(this, FloatMenuService::class.java)
        val details = Intent(Settings.ACTION_ACCESSIBILITY_DETAILS_SETTINGS)
            .putExtra(Intent.EXTRA_COMPONENT_NAME, component.flattenToString())
        try {
            startActivity(details)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }
}
