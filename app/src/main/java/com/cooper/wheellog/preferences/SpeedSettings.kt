package com.cooper.wheellog.preferences

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import com.cooper.wheellog.R
import com.cooper.wheellog.WheelLog
import com.cooper.wheellog.presentation.preferences.MultiSelectPreference
import com.cooper.wheellog.presentation.preferences.SeekBarPreference
import com.cooper.wheellog.utils.ThemeEnum
import com.cooper.wheellog.utils.ThemeIconEnum
import timber.log.Timber

class SpeedSettings(context: Context, ps: PreferenceScreen) : BaseSettingsClass(context, ps) {

    override fun fill(mac: String) {
        ps.removeAll()
        SwitchPreference(context).apply {
            key = getString(R.string.use_eng)
            title = getString(R.string.use_eng_title)
            summary = getString(R.string.use_eng_description)
            icon = getDrawable(ThemeIconEnum.SettingsLanguage)
            ps.addPreference(this)
        }
        ListPreference(context).apply {
            key = getString(R.string.app_theme)
            title = getString(R.string.app_theme_title)
            summary = getString(R.string.app_theme_description)
            entries = ThemeEnum.values().map { x -> x.name }.toTypedArray()
            entryValues = ThemeEnum.values().map { x -> x.value.toString() }.toTypedArray()
            ps.addPreference(this)
        }
        ListPreference(context).apply {
            key = getString(R.string.day_night_theme)
            title = getString(R.string.day_night_theme_title)
            summary = when (WheelLog.AppConfig.dayNightThemeMode) {
                AppCompatDelegate.MODE_NIGHT_NO -> getString(R.string.day_night_theme_day)
                AppCompatDelegate.MODE_NIGHT_YES -> getString(R.string.day_night_theme_night)
                else -> getString(R.string.day_night_theme_as_system)
            }
            entries = arrayOf(
                getString(R.string.day_night_theme_as_system),
                getString(R.string.day_night_theme_day),
                getString(R.string.day_night_theme_night)
            )
            entryValues = arrayOf(
                AppCompatDelegate.MODE_NIGHT_UNSPECIFIED,
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES
            )
                .map { x -> x.toString() }.toTypedArray()
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.use_better_percents)
            title = getString(R.string.use_better_percents_title)
            summary = getString(R.string.use_better_percents_description)
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.custom_percents)
            title = getString(R.string.custom_percents_title)
            summary = getString(R.string.custom_percents_description)
            ps.addPreference(this)
            dependency = getString(R.string.use_better_percents)
        }
        SeekBarPreference(context).apply {
            key = mac + getString(R.string.cell_voltage_tiltback)
            title = getString(R.string.cell_voltage_tiltback_title)
            summary = getString(R.string.cell_voltage_tiltback_description)
            min = 250
            max = 400
            unit = getString(R.string.volt)
            increment = 1
            decimalPlaces = 2
            setDefaultValue(WheelLog.AppConfig.cellVoltageTiltback)
            ps.addPreference(this)
            dependency = getString(R.string.custom_percents)
        }
        PreferenceCategory(context).apply {
            title = getString(R.string.measurement_systems_category_title)
            ps.addPreference(this)
            SwitchPreference(context).apply {
                key = getString(R.string.use_mph)
                title = getString(R.string.use_mph_title)
                summary = getString(R.string.use_mph_description)
                ps.addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.use_fahrenheit)
                title = getString(R.string.use_fahrenheit_title)
                summary = getString(R.string.use_fahrenheit_description)
                ps.addPreference(this)
            }
        }
        PreferenceCategory(context).apply {
            title = getString(R.string.after_connect_category)
            ps.addPreference(this)
            SwitchPreference(context).apply {
                key = getString(R.string.auto_log)
                title = getString(R.string.auto_log_title)
                summary = getString(R.string.auto_log_description)
                icon = getDrawable(ThemeIconEnum.SettingsAutoLog)
                addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.auto_watch)
                title = getString(R.string.auto_watch_title)
                summary = getString(R.string.auto_watch_description)
                icon = getDrawable(ThemeIconEnum.SettingsWatch)
                addPreference(this)
            }
        }
        PreferenceCategory(context).apply {
            title = getString(R.string.main_view_category)
            ps.addPreference(this)
            MultiSelectPreference(context).apply {
                key = getString(R.string.view_blocks_string)
                title = getString(R.string.view_blocks_title)
                summary = getString(R.string.view_blocks_description)
                icon = getDrawable(ThemeIconEnum.SettingsBlocks)
                dialogTitle = title
                dialogIcon = icon
                setValues(WheelLog.AppConfig.viewBlocks)
                entries = context.resources.getTextArray(R.array.view_blocks_values)
                addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.use_pip_mode)
                title = getString(R.string.use_pip_mode_title)
                summary = getString(R.string.use_pip_mode_description)
                setDefaultValue(WheelLog.AppConfig.usePipMode)
                addPreference(this)
            }
            ListPreference(context).apply {
                key = getString(R.string.pip_block)
                title = getString(R.string.pip_block_title)
                summary = WheelLog.AppConfig.pipBlock
                dialogTitle = title
                entries = arrayOf(
                    getString(R.string.speed),
                    getString(R.string.consumption)
                )
                entryValues = entries
                isVisible = WheelLog.AppConfig.usePipMode
                addPreference(this)
                dependency = getString(R.string.use_pip_mode)
            }
            MultiSelectPreference(context).apply {
                key = getString(R.string.notification_buttons)
                title = getString(R.string.notification_buttons_title)
                summary = getString(R.string.notification_buttons_description)
                icon = getDrawable(ThemeIconEnum.SettingsNotification)
                dialogTitle = title
                dialogIcon = icon
                setValues(WheelLog.AppConfig.notificationButtons)
                entries = arrayOf(
                    getString(R.string.icon_connection),
                    getString(R.string.icon_logging),
                    getString(R.string.icon_watch),
                    getString(R.string.icon_beep),
                    getString(R.string.icon_light),
                    getString(R.string.icon_miband)
                )
                useSort = false
                addPreference(this)
            }
            SeekBarPreference(context).apply {
                key = getString(R.string.max_speed)
                title = getString(R.string.max_speed_dial_title)
                summary = getString(R.string.max_speed_dial_description)
                min = 10
                max = 100
                increment = 1
                setDefaultValue(WheelLog.AppConfig.maxSpeed)
                addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.current_on_dial)
                title = getString(R.string.current_on_dial_title)
                summary = getString(R.string.current_on_dial_description)
                addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.use_short_pwm)
                title = getString(R.string.use_short_pwm_title)
                summary = getString(R.string.use_short_pwm_description)
                setDefaultValue(WheelLog.AppConfig.useShortPwm)
                addPreference(this)
            }
        }
        SwitchPreference(context).apply {
            key = getString(R.string.show_page_graph)
            title = getString(R.string.show_page_graph_title)
            setDefaultValue(WheelLog.AppConfig.pageGraph)
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.show_page_events)
            title = getString(R.string.show_page_events_title)
            summary = getString(R.string.show_page_events_description)
            icon = getDrawable(ThemeIconEnum.SettingsPageEvents)
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.show_page_trips)
            title = getString(R.string.show_page_trips_title)
            icon = getDrawable(ThemeIconEnum.SettingsPageTrips)
            setDefaultValue(WheelLog.AppConfig.pageTrips)
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.connection_sound)
            title = getString(R.string.connection_sound_title)
            summary = getString(R.string.connection_sound_description)
            icon = getDrawable(ThemeIconEnum.SettingsConnectionSound)
            setDefaultValue(WheelLog.AppConfig.connectionSound)
            ps.addPreference(this)
        }
        SeekBarPreference(context).apply {
            key = getString(R.string.no_connection_sound)
            title = getString(R.string.no_connection_sound_title)
            summary = getString(R.string.no_connection_sound_description)
            min = 0
            max = 60
            unit = getString(R.string.sec)
            increment = 1
            setDefaultValue(WheelLog.AppConfig.noConnectionSound)
            ps.addPreference(this)
            dependency = getString(R.string.connection_sound)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.use_stop_music)
            title = getString(R.string.use_stop_music_title)
            summary = getString(R.string.use_stop_music_description)
            icon = getDrawable(ThemeIconEnum.SettingsAutoMute)
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.show_unknown_devices)
            title = getString(R.string.show_unknown_devices_title)
            summary = getString(R.string.show_unknown_devices_description)
            setDefaultValue(WheelLog.AppConfig.showUnknownDevices)
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.use_reconnect)
            title = getString(R.string.use_reconnect_title)
            summary = getString(R.string.use_reconnect_description)
            ps.addPreference(this)
        }
        PreferenceCategory(context).apply {
            title = getString(R.string.beep_category)
            ps.addPreference(this)
            SwitchPreference(context).apply {
                key = getString(R.string.beep_on_single_tap)
                title = getString(R.string.beep_on_single_tap_title)
                summary = getString(R.string.beep_on_single_tap_description)
                setDefaultValue(WheelLog.AppConfig.useBeepOnSingleTap)
                addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.beep_on_volume_up)
                title = getString(R.string.beep_on_volume_up_title)
                summary = getString(R.string.beep_on_volume_up_description)
                setDefaultValue(WheelLog.AppConfig.useBeepOnVolumeUp)
                addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.beep_by_wheel)
                title = getString(R.string.beep_by_wheel_title)
                summary = getString(R.string.beep_by_wheel_description)
                setDefaultValue(WheelLog.AppConfig.beepByWheel)
                disableDependentsState = true
                addPreference(this)
            }
            SwitchPreference(context).apply {
                key = getString(R.string.custom_beep)
                title = getString(R.string.custom_beep_title)
                setDefaultValue(WheelLog.AppConfig.useCustomBeep)
                addPreference(this)
                dependency = getString(R.string.beep_by_wheel)
            }
        }
        SwitchPreference(context).apply {
            key = getString(R.string.use_detect_battery_optimization)
            title = getString(R.string.use_detect_battery_optimization_title)
            setDefaultValue(WheelLog.AppConfig.detectBatteryOptimization)
            ps.addPreference(this)
        }
        SwitchPreference(context).apply {
            key = getString(R.string.yandex_metriсa_accepted)
            title = getString(R.string.send_yandex_metriсa_title)
            summary = getString(R.string.send_yandex_metriсa)
            setDefaultValue(WheelLog.AppConfig.yandexMetricaAccepted)
            ps.addPreference(this)
        }
    }

    fun selectCustomBeep(fragment: PreferencesFragment) {
        if (!WheelLog.AppConfig.useCustomBeep) {
            return
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            // Android 11+
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            if (uri == null) {
                Timber.wtf(getString(R.string.no_file_read_permission))
                WheelLog.AppConfig.useCustomBeep = false
                return
            }
            val projection = arrayOf(MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media._ID)
            val cursor = context.contentResolver.query(uri,
                    projection,
                    null,
                    null,
                    MediaStore.Downloads.DATE_MODIFIED + " DESC")
            val sounds = mutableMapOf<String, String>()
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val title = cursor.getString(0)
                    val mediaId = cursor.getString(1)
                    sounds[title] = mediaId
                } while (cursor.moveToNext())
                cursor.close()
            }
            if (sounds.isNotEmpty()) {
                val fileNames = sounds.keys.toTypedArray()
                AlertDialog.Builder(context)
                        .setTitle(getString(R.string.custom_beep_title))
                        .setItems(fileNames) { _, which ->
                            val fileName = fileNames[which]
                            val id = sounds[fileName]
                            fragment.findPreference<SwitchPreference>(getString(R.string.custom_beep))?.summary = fileName
                            WheelLog.AppConfig.beepFile = Uri.withAppendedPath(uri, id)
                        }
                        .setCancelable(false)
                        .create()
                        .show()
            } else {
                Timber.wtf(getString(R.string.files_not_found))
                WheelLog.AppConfig.useCustomBeep = false
            }
        } else
            // Android 10 or less
            fragment.mediaRequestResult.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "audio/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            })
    }
}