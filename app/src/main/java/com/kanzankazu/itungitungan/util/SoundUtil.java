package com.kanzankazu.itungitungan.util;

import android.content.Context;
import android.media.AudioManager;

public class SoundUtil {
    public static int getVolume(Context activity, int streamType) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(streamType);
    }

    public static void setVolumeMax(Context activity, int streamType) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(streamType);
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(streamType, streamMaxVolume, 0);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(streamType, streamMaxVolume, 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                audioManager.setStreamVolume(streamType, streamMaxVolume, 0);

                break;
        }
    }

    public static void setVolumeMin(Context activity, int streamType) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = 0;
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(streamType, streamMaxVolume, 0);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(streamType, streamMaxVolume, 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                audioManager.setStreamVolume(streamType, streamMaxVolume, 0);
                break;
        }
    }

    public static void setVolumeTo(Context activity, int streamType, int volume) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(streamType, volume, 0);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audioManager.setStreamVolume(streamType, volume, 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                audioManager.setStreamVolume(streamType, volume, 0);
                break;
        }
    }

    public static int getRingerMode(Context activity) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    public static void setRingerMode(Context activity, int ringMode) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(ringMode);
    }

}
