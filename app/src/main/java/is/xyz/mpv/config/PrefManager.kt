package `is`.xyz.mpv.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

object PrefManager {

    lateinit var prefs: SharedPreferences
        private set

    fun init(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    /** General Settings Preferences **/
    var default_file_manager_path: String?
        @SuppressLint("SdCardPath")
        get() = prefs.getString("default_file_manager_path", "/sdcard")
        set(value) {
            prefs.edit { putString("default_file_manager_path", value) }
        }

    var default_audio_language: String?
        get() = prefs.getString("default_audio_language", "english")
        set(value) {
            prefs.edit { putString("default_audio_language", value) }
        }

    var default_subtitle_language: String?
        get() = prefs.getString("default_subtitle_language", "english")
        set(value) {
            prefs.edit { putString("default_subtitle_language", value) }
        }

    var hardware_decoding: Boolean
        get() = prefs.getBoolean("hardware_decoding", true)
        set(value) {
            prefs.edit { putBoolean("hardware_decoding", value) }
        }

    var background_play: String?
        get() = prefs.getString("background_play", "never")
        set(value) {
            prefs.edit { putString("background_play", value) }
        }

    var save_position: Boolean
        get() = prefs.getBoolean("save_position", false)
        set(value) {
            prefs.edit { putBoolean("save_position", value) }
        }

    var ignore_audio_focus: Boolean
        get() = prefs.getBoolean("ignore_audio_focus", false)
        set(value) {
            prefs.edit { putBoolean("ignore_audio_focus", value) }
        }

    var gpudebug: Boolean
        get() = prefs.getBoolean("gpudebug", false)
        set(value) {
            prefs.edit { putBoolean("gpudebug", value) }
        }

    var stats_mode: String?
        get() = prefs.getString("stats_mode", "")
        set(value) {
            prefs.edit { putString("stats_mode", value) }
        }
}
