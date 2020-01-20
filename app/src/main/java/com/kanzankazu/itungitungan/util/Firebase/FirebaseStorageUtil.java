package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseStorageUtil {

    private static StorageReference getRootRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    /*Upload*/
    public static void uploadImages(Activity mActivity, String STORAGE_PATH, ArrayList<Uri> uris, DoneListener listener) {
        ArrayList<String> imageDonwloadUrls = new ArrayList<>();
        ArrayList<String> listStat = new ArrayList<>();
        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);

            if (uri != null) {
                ProgressDialog progressDialog = new ProgressDialog(mActivity);
                progressDialog.setTitle("Menyimpan Gambar");
                progressDialog.show();

                StorageReference reference = getRootRef().child(STORAGE_PATH + System.currentTimeMillis() + "." + "jpg" /*PictureUtil2.getFileExtension(mActivity, uri)*/);
                reference.putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> {
                            progressDialog.dismiss();
                            String imageDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                            imageDonwloadUrls.add(imageDownloadUrl);
                            listStat.add("1");

                            int frequency0 = Collections.frequency(listStat, "0");
                            int frequency1 = Collections.frequency(listStat, "1");

                            if (frequency1 == uris.size()) {
                                listener.isFinised(imageDonwloadUrls);
                            }
                        })
                        .addOnFailureListener(exception -> {
                            progressDialog.dismiss();
                            listStat.add("0");

                            int frequency0 = Collections.frequency(listStat, "0");
                            int frequency1 = Collections.frequency(listStat, "1");

                            if (frequency0 == uris.size()) {
                                listener.isFailed(exception.getMessage());
                            }
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        })
                ;
            }
        }
    }

    /*Delete*/
    public static void deleteImages(Activity mActivity, List<String> urls, DoneRemoveListener listener) {
        ArrayList<String> listStat = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);

            if (url != null) {
                ProgressDialog progressDialog = new ProgressDialog(mActivity);
                progressDialog.setTitle("Menghapus Gambar");
                progressDialog.show();

                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                //StorageReference reference = getRootRef().child(STORAGE_PATH + System.currentTimeMillis() + "." + "jpg" /*PictureUtil2.getFileExtension(mActivity, uri)*/);
                reference.delete()
                        .addOnSuccessListener(taskSnapshot -> {
                            progressDialog.dismiss();
                            listStat.add("1");

                            int frequency0 = Collections.frequency(listStat, "0");
                            int frequency1 = Collections.frequency(listStat, "1");

                            if (frequency1 == urls.size()) {
                                listener.isFinised();
                            }
                        })
                        .addOnFailureListener(exception -> {
                            progressDialog.dismiss();
                            listStat.add("0");

                            int frequency0 = Collections.frequency(listStat, "0");
                            int frequency1 = Collections.frequency(listStat, "1");

                            if (frequency0 == urls.size()) {
                                listener.isFailed(exception.getMessage());
                            }
                        })
                ;
            }
        }
    }

    /*interface*/
    public static interface DoneListener {
        void isFinised(ArrayList<String> imageDonwloadUrls);

        void isFailed(String message);
    }

    public static interface DoneRemoveListener {
        void isFinised();

        void isFailed(String message);
    }
}
