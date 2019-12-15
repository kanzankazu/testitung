package com.kanzankazu.itungitungan.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.kanzankazu.itungitungan.BuildConfig;
import id.zelory.compressor.Compressor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class PictureUtil {

    public final int REQUEST_IMAGE_CAMERA = 1;
    public final int REQUEST_IMAGE_GALLERY = 2;
    public String[] permCameraGallery = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private String mCurrentPhotoPath;
    private AppCompatActivity mActivity;
    private ImageView imageView;

    public PictureUtil(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void chooseGetImageDialog(ImageView imageView) {
        this.imageView = imageView;

        final String[] items = {"Pilih foto dari kamera", "Pilih foto dari galeri"};

        AlertDialog.Builder chooseImageDialog = new AlertDialog.Builder(mActivity);
        chooseImageDialog.setItems(items, (dialogInterface, i) -> {
            if (items[i].equals("Pilih foto dari kamera")) {
                openCamera();
            } else {
                openGallery();
            }
        });

        chooseImageDialog.show();
    }

    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("data on activity result : " + data);
        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK && mCurrentPhotoPath != null) { //FROM CAMERA
            try {
                compressImage();
                new Handler().postDelayed(() -> {
                    //code here
                    Glide.with(mActivity).load(mCurrentPhotoPath).into(imageView);
                }, 500);//interval

                /*documentDataList.get(currentIndex).setPhotoPath(mCurrentPhotoPath);
                preApprovalUploadAdapter.addToList(currentIndex, mCurrentPhotoPath);

                for (int i = 0; i < documentDataList.size(); i++) {
                    if (documentDataList.get(i).getPhotoPath().isEmpty()) {
                        submitBtn.setEnabled(false);
                        break;
                    } else {
                        submitBtn.setEnabled(true);
                    }
                }*/

                return mCurrentPhotoPath;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) { //FROM GALLERY
            try {
                Uri uri = data.getData();

                String galleryPath = getRealPathFromURIPath(uri);
                File file = new File(Uri.parse(galleryPath).getPath());
                try {
                    File compressedImage = new Compressor(mActivity).setQuality(50).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(file);
                    mCurrentPhotoPath = getRealPathFromURIPath(Uri.parse(compressedImage.getAbsolutePath()));
                    Glide.with(mActivity).load(mCurrentPhotoPath).into(imageView);

                    /*CompressBitmap cb = new CompressBitmap(mActivity);
                    String filepath = cb.getRealPathFromURI(mActivity, data.getData());
                    Bitmap bitmap = cb.compressImage(cb.getRealPathFromURI(mActivity, data.getData()), filepath);*/

                    /*documentDataList.get(currentIndex).setPhotoPath(String.valueOf(uri));
                    preApprovalUploadAdapter.addToList(currentIndex, String.valueOf(uri));

                    for (int i = 0; i < documentDataList.size(); i++) {
                        if (documentDataList.get(i).getPhotoPath().isEmpty()) {
                            submitBtn.setEnabled(false);
                            break;
                        } else {
                            submitBtn.setEnabled(true);
                        }
                    }*/

                    return mCurrentPhotoPath;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void openCamera() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        mCurrentPhotoPath = photoFile.getAbsolutePath();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            // only for nougat and above camera
                            try {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mActivity,
                                        BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                                mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
                        }
                    }
                }
            }
        },500);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        mActivity.startActivityForResult(Intent.createChooser(intent, "Pilih foto"), REQUEST_IMAGE_GALLERY);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void compressImage() {
        File file = new File(Uri.parse(mCurrentPhotoPath).getPath());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap original = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 1440, 1440, true);

        try {
            ExifInterface ei = new ExifInterface(Uri.parse(mCurrentPhotoPath).getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            System.out.println("photo orientation : " + orientation);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    original = rotateImage(original, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    original = rotateImage(original, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    original = rotateImage(original, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    //original = original;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            original.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void compressImage(String mCurrentPhotoPath) {
        File file = new File(Uri.parse(mCurrentPhotoPath).getPath());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap original = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 1440, 1440, true);

        try {
            ExifInterface ei = new ExifInterface(Uri.parse(mCurrentPhotoPath).getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    original = rotateImage(original, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    original = rotateImage(original, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    original = rotateImage(original, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    //original = original;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            original.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private String getRealPathFromURIPath(Uri contentURI) {
        Cursor cursor = mActivity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


}
