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

    /**
     * General Preferences
     */
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

    /**
     * Developer Preferences
     */
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

    /**
     * Gesture Preferences
     */
    var seek_gesture_smooth: Boolean
        get() = prefs.getBoolean("seek_gesture_smooth", false)
        set(value) {
            prefs.edit { putBoolean("seek_gesture_smooth", value) }
        }
    var gesture_horiz: String?
        get() = prefs.getString("gesture_horiz", "seek")
        set(value) {
            prefs.edit { putString("gesture_horiz", value) }
        }

    var gesture_vert_left: String?
        get() = prefs.getString("gesture_vert_left", "bright")
        set(value) {
            prefs.edit { putString("gesture_vert_left", value) }
        }

    var gesture_vert_right: String?
        get() = prefs.getString("gesture_vert_right", "volume")
        set(value) {
            prefs.edit { putString("gesture_vert_right", value) }
        }

    var gesture_tap_left: String?
        get() = prefs.getString("gesture_tap_left", "none")
        set(value) {
            prefs.edit { putString("gesture_tap_left", value) }
        }

    var gesture_tap_center: String?
        get() = prefs.getString("gesture_tap_center", "none")
        set(value) {
            prefs.edit { putString("gesture_tap_center", value) }
        }

    var gesture_tap_right: String?
        get() = prefs.getString("gesture_tap_right", "none")
        set(value) {
            prefs.edit { putString("gesture_tap_right", value) }
        }

    /**
     * UI Preferences
     */
    var auto_rotation: String?
        get() = prefs.getString("auto_rotation", "auto")
        set(value) {
            prefs.edit { putString("auto_rotation", value) }
        }

    var display_media_title: Boolean
        get() = prefs.getBoolean("display_media_title", false)
        set(value) {
            prefs.edit { putBoolean("display_media_title", value) }
        }

    var bottom_controls: Boolean
        get() = prefs.getBoolean("bottom_controls", true)
        set(value) {
            prefs.edit { putBoolean("bottom_controls", value) }
        }

    var no_ui_pause: String?
        get() = prefs.getString("no_ui_pause", "audio-only")
        set(value) {
            prefs.edit { putString("no_ui_pause", value) }
        }

    /**
     * Video Preferences
     */
    var video_scale: String?
        get() = prefs.getString("video_scale", "")
        set(value) {
            prefs.edit { putString("video_scale", value) }
        }

    var video_scale_param1: String?
        get() = prefs.getString("video_scale_param1", "")
        set(value) {
            prefs.edit { putString("video_scale_param1", value) }
        }

    var video_scale_param2: String?
        get() = prefs.getString("video_scale_param2", "")
        set(value) {
            prefs.edit { putString("video_scale_param2", value) }
        }

    var video_downscale: String?
        get() = prefs.getString("video_downscale", "")
        set(value) {
            prefs.edit { putString("video_downscale", value) }
        }

    var video_downscale_param1: String?
        get() = prefs.getString("video_downscale_param1", "")
        set(value) {
            prefs.edit { putString("video_downscale_param1", value) }
        }

    var video_downscale_param2: String?
        get() = prefs.getString("video_downscale_param2", "")
        set(value) {
            prefs.edit { putString("video_downscale_param2", value) }
        }

    var video_debanding: String?
        get() = prefs.getString("video_debanding", "")
        set(value) {
            prefs.edit { putString("video_debanding", value) }
        }

    var video_tscale: String?
        get() = prefs.getString("video_tscale", "")
        set(value) {
            prefs.edit { putString("video_tscale", value) }
        }

    var video_tscale_param1: String?
        get() = prefs.getString("video_tscale_param1", "")
        set(value) {
            prefs.edit { putString("video_tscale_param1", value) }
        }

    var video_tscale_param2: String?
        get() = prefs.getString("video_tscale_param2", "")
        set(value) {
            prefs.edit { putString("video_tscale_param2", value) }
        }

    var video_fastdecode: Boolean
        get() = prefs.getBoolean("video_fastdecode", false)
        set(value) {
            prefs.edit { putBoolean("video_fastdecode", value) }
        }

    var video_interpolation: Boolean
        get() = prefs.getBoolean("video_interpolation", false)
        set(value) {
            prefs.edit { putBoolean("video_interpolation", value) }
        }

    var video_sync: String?
        get() = prefs.getString("video_sync", "audio")
        set(value) {
            prefs.edit { putString("video_sync", value) }
        }
}
