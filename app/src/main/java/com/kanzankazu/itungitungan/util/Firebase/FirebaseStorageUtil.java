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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FirebaseStorageUtil {
    private Activity mActivity;

    public FirebaseStorageUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    private StorageReference getRootRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    /*Upload*/
    public void uploadImage(String STORAGE_PATH, Uri filePath, OnSuccessListener successListener, OnFailureListener failureListener, OnProgressListener progressListener) {
        //ProgressDialog progressDialog = new ProgressDialog(activity);
        //progressDialog.setTitle("Uploading");
        //progressDialog.show();
        StorageReference reference = getRootRef().child(STORAGE_PATH + System.currentTimeMillis() + "." + PictureUtil2.getFileExtension(mActivity, filePath));
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

    public boolean uploadImage(String STORAGE_PATH, Uri filePath) {
        final Boolean[] isFinish = new Boolean[0];

        ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference reference = getRootRef().child(STORAGE_PATH + System.currentTimeMillis() + "." + PictureUtil2.getFileExtension(mActivity, filePath));
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

    public void uploadImages(String STORAGE_PATH, ArrayList<Uri> uris, DoneListener listener) {
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

    public ArrayList<String> uploadImages(String STORAGE_PATH, Uri... uris) {
        /*ArrayList<Uri> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
        }*/
        ArrayList<Uri> arrayList = new ArrayList<>(Arrays.asList(uris));
        return uploadImages(STORAGE_PATH, arrayList);
    }

    public ArrayList<String> uploadImages(String STORAGE_PATH, ArrayList<Uri> uris) {
        ArrayList<String> imageDonwloadUrls = new ArrayList<>();
        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);

            ProgressDialog progressDialog = new ProgressDialog(mActivity);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference reference = getRootRef().child(STORAGE_PATH + System.currentTimeMillis() + "." + PictureUtil2.getFileExtension(mActivity, uri));
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

    /*Delete*/
    public void deleteImage(String url, OnSuccessListener successListener, OnFailureListener failureListener) {
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        reference
                .delete()
                .addOnSuccessListener(
                        successListener
                )
                .addOnFailureListener(
                        failureListener
                );
    }

    public boolean deleteImage(String url) {
        final Boolean[] isFinish = new Boolean[0];

        ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setTitle("Deleting");
        progressDialog.show();

        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        reference
                .delete()
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
                );
        return isFinish[0];
    }

    public void deleteImages1(List<String> urls, DoneRemoveListener listener) {
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

    public void deleteImages2(@NotNull List<String> urls, @NotNull DoneRemoveListener listener) {
        deleteImages1(urls, listener);
    }

    public ArrayList<String> deleteImages(String... uris) {
        /*ArrayList<Uri> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
        }*/
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(uris));
        return deleteImages(arrayList);
    }

    public ArrayList<String> deleteImages(ArrayList<String> uris) {
        ArrayList<String> imageDonwloadUrls = new ArrayList<>();
        for (int i = 0; i < uris.size(); i++) {
            String url = uris.get(i);

            ProgressDialog progressDialog = new ProgressDialog(mActivity);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            reference
                    .delete()
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                    })
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
            ;
        }
        return imageDonwloadUrls;
    }

    /*interface*/
    public interface DoneListener {
        void isFinised(ArrayList<String> imageDonwloadUrls);

        void isFailed(String message);
    }

    public interface DoneRemoveListener {
        void isFinised();

        void isFailed(String message);
    }
}
