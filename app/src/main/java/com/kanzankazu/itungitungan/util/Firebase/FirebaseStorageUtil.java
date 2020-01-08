package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.kanzankazu.itungitungan.util.PictureUtil2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FirebaseStorageUtil {
    private Activity mActivity;

    public FirebaseStorageUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public StorageReference getRootRef() {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        return reference;
    }

    public void uploadImage(String STORAGE_PATH_UPLOADS, Uri filePath, OnSuccessListener successListener, OnFailureListener failureListener, OnProgressListener progressListener) {
        //ProgressDialog progressDialog = new ProgressDialog(activity);
        //progressDialog.setTitle("Uploading");
        //progressDialog.show();
        StorageReference reference = getRootRef().child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + PictureUtil2.getFileExtension(mActivity, filePath));
        reference
                .putFile(filePath)
                .addOnSuccessListener(
                        successListener
                        //progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        //Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                        //String uploadId = mDatabase.push().getKey();
                        //mDatabase.child(uploadId).setValue(upload);
                )
                .addOnFailureListener(
                        failureListener
                        //progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                )
                .addOnProgressListener(
                        progressListener
                        //displaying the upload progress
                        //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                );
    }

    public boolean uploadImage(String STORAGE_PATH_UPLOADS, Uri filePath) {
        final Boolean[] isFinish = new Boolean[0];

        ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference reference = getRootRef().child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + PictureUtil2.getFileExtension(mActivity, filePath));
        reference
                .putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                            progressDialog.dismiss();
                            Toast.makeText(mActivity, "File Uploaded ", Toast.LENGTH_LONG).show();
                            //Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                            //String uploadId = mDatabase.push().getKey();
                            //mDatabase.child(uploadId).setValue(upload);
                            isFinish[0] = true;
                        }
                )
                .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                            isFinish[0] = false;
                        }
                )
                .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                );
        return isFinish[0];
    }

    public ArrayList<String> uploadImages(String STORAGE_PATH_UPLOADS, ArrayList<Uri> uris) {
        ArrayList<String> imageDonwloadUrls = new ArrayList<>();
        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);

            ProgressDialog progressDialog = new ProgressDialog(mActivity);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference reference = getRootRef().child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + PictureUtil2.getFileExtension(mActivity, uri));
            reference
                    .putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        String imageDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                        imageDonwloadUrls.add(imageDownloadUrl);
                    })
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    })
            ;
        }
        return imageDonwloadUrls;
    }

    public void uploadImages(String STORAGE_PATH_UPLOADS, ArrayList<Uri> uris, DoneListener listener) {
        ArrayList<String> imageDonwloadUrls = new ArrayList<>();
        ArrayList<String> listStat = new ArrayList<>();
        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);

            ProgressDialog progressDialog = new ProgressDialog(mActivity);
            progressDialog.setTitle("Menyimpan Gambar");
            progressDialog.show();

            StorageReference reference = getRootRef().child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + "jpg" /*PictureUtil2.getFileExtension(mActivity, uri)*/);
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
                            listener.isFailed();
                        }
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    })
            ;
        }
    }

    public ArrayList<String> uploadImages(String STORAGE_PATH_UPLOADS, Uri... uris) {
        /*ArrayList<Uri> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
        }*/
        ArrayList<Uri> arrayList = new ArrayList<>(Arrays.asList(uris));
        return uploadImages(STORAGE_PATH_UPLOADS, arrayList);
    }

    public interface DoneListener {
        void isFinised(ArrayList<String> imageDonwloadUrls);

        void isFailed();
    }
}
