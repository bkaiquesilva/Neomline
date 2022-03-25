package com.bkaiquesilva.nnzi;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GravarAudio {

        private boolean singleFile = true;

        public MediaRecorder recorder;

        private Context context;

        private ArrayList<String> files = new ArrayList<String>();

        private String fileDirectory;

    private String a;
    private String b;
    private String c;

        private String finalAudioPath;

        private boolean isRecording;

        public boolean isStarted;

        public boolean isRecording() {
            return isRecording;
        }

        public String getAudioFilePath() {
            return finalAudioPath;
        }

        public GravarAudio(String audioFileDirectory, Context context, String a, String b, String c) {
            this.fileDirectory = audioFileDirectory;
            this.context = context;
            this.a = a;
            this.b = b;
            this.c = c;

            if (!this.fileDirectory.endsWith("/")) {
                this.fileDirectory += "/";
            }

            newRecorder();
        }

        public boolean start() {
            prepareRecorder();

            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            if (recorder != null) {
                recorder.start();
                isRecording = true;
                isStarted = true;
                return true;
            }

            return false;
        }

        public boolean pause() {
            if (recorder == null || !isRecording) {
                throw new IllegalStateException("[AudioRecorder] recorder is not recording!");
            }

            recorder.stop();
            recorder.release();
            recorder = null;

            isRecording = false;

            return merge();
        }

        public boolean resume() {
            if (isRecording) {
                throw new IllegalStateException("[AudioRecorder] recorder is recording!");
            }

            singleFile = false;
            newRecorder();
            return start();
        }

        public boolean stop() {
            if (!isRecording) {
                return merge();
            }

            if (recorder == null) {
                return false;
            }

            recorder.stop();
            recorder.release();
            recorder = null;
            isRecording = false;
            isStarted = false;
            return merge();
        }

        public void clear() {
            if (recorder != null || isRecording) {
                recorder.stop();
                recorder.release();
                recorder = null;
                isRecording = false;
            }
            for (int i = 0, len = files.size(); i < len; i++) {
                File file = new File(this.files.get(i));
                file.delete();
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
            }
            finalAudioPath = "";
        }

        private boolean merge() {

            // If never paused, just return the file
            if (singleFile) {
                this.finalAudioPath = this.files.get(0);
                return true;
            }

            // Merge files
            String mergedFilePath = this.fileDirectory + System.currentTimeMillis() + ".amr";
            try {
                FileOutputStream fos = new FileOutputStream(mergedFilePath);

                for (int i = 0, len = files.size(); i < len; i++) {

                    try {
                        File file = new File(this.files.get(i));
                        FileInputStream fis = new FileInputStream(file);

                        // Skip file header bytes,
                        // amr file header's length is 6 bytes
                        if (i > 0) {
                            for (int j = 0; j < 6; j++) {
                                fis.read();
                            }
                        }

                        byte[] buffer = new byte[512];
                        int count = 0;
                        while ((count = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
                        }

                        fis.close();
//                    fos.flush();
//                    file.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fos.flush();
                fos.close();
//            files.clear();
                this.finalAudioPath = mergedFilePath;
//            files.add(this.finalAudioPath)

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        private void newRecorder() {
            recorder = new MediaRecorder();
        }

        private void prepareRecorder() {
            File directory = new File(this.fileDirectory);
            if (!directory.exists() || !directory.isDirectory()) {
                throw new IllegalArgumentException("[AudioRecorder] audioFileDirectory is a not valid directory!");
            }

            String filePath = directory.getAbsolutePath() + "/" + System.currentTimeMillis() + ".amr";
            this.files.add(filePath);

            recorder.setOutputFile(filePath);
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }
}
