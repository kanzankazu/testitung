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
import com.kanzankazu.itungitungan.util.PictureUtil;

import java.util.ArrayList;

public class FirebaseStorageUtil {
    public FirebaseStorageUtil() {
    }

    public static StorageReference getRootRef() {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        return reference;
    }

    public static void uploadImage(Activity activity, String STORAGE_PATH_UPLOADS, Uri filePath, OnSuccessListener successListener, OnFailureListener failureListener, OnProgressListener progressListener) {
        //ProgressDialog progressDialog = new ProgressDialog(activity);
        //progressDialog.setTitle("Uploading");
        //progressDialog.show();
        StorageReference reference = getRootRef().child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + PictureUtil.getFileExtension(activity, filePath));
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

    public static boolean uploadImage(Activity activity, String STORAGE_PATH_UPLOADS, Uri filePath) {
        final Boolean[] isFinish = new Boolean[0];

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference reference = getRootRef().child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + PictureUtil.getFileExtension(activity, filePath));
        reference
                .putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "File Uploaded ", Toast.LENGTH_LONG).show();
                            //Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                            //String uploadId = mDatabase.push().getKey();
                            //mDatabase.child(uploadId).setValue(upload);
                            isFinish[0] = true;
                        }
                )
                .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
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

    public static ArrayList<String> uploadImages(Activity activity, String STORAGE_PATH_UPLOADS, ArrayList<Uri> uris) {
        ArrayList<String> imageDonwloadUrls = new ArrayList<>();
        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);

            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference reference = getRootRef().child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + PictureUtil.getFileExtension(activity, uri));
            reference
                    .putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        String imageDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                        imageDonwloadUrls.add(imageDownloadUrl);
                    })
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    })
            ;
        }
        return imageDonwloadUrls;
    }
}
