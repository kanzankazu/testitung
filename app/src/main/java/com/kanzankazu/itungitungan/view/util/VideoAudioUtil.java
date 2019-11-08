package com.kanzankazu.itungitungan.view.util;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class VideoAudioUtil {
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    public static MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.e("Lihat", "onError AlarmActivity : " + what);
            Log.e("Lihat", "onError AlarmActivity : " + extra);
        }
    };
    public static MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.e("Lihat", "onError AlarmActivity : " + what);
            Log.e("Lihat", "onError AlarmActivity : " + extra);
        }
    };
    private static String voicePath;

    public static void MediaRecorderReady(MediaRecorder recorder) {
        //recorder = new MediaRecorder();
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setOutputFile(getFilename());
        } catch (Exception e) {
            Log.e("Lihat", "MediaRecorderReady VideoAudioUtil : " + e);
        }
    }

    public static void startRecording(MediaRecorder recorder) {
        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String stopRecording(MediaRecorder recorder) {
        try {
            recorder.stop();
            recorder.reset();
            //recorder.release();
            recorder = null;
        } catch (RuntimeException stopException) {
            //handle cleanup here
            Log.i("Lihat", "stopRecording AlarmActivity : " + stopException.getMessage());
            Log.i("Lihat", "stopRecording AlarmActivity : " + stopException.toString());
        }
        /*recorder.stop();
        recorder.release();*/

        return voicePath;
    }

    public static String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();

        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        voicePath = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3";
        ;
        Log.d("Lihat", "getFilename AlarmActivity : " + voicePath);
        return (voicePath);
    }
}
