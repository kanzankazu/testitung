package com.kanzankazu.itungitungan.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class PictureUtil2 {

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

    public PictureUtil2(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    public static String getFileExtension(Activity activity, Uri uri) {
        ContentResolver cR = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public static ArrayList<Uri> convertArrayUriToArrayListUri(Uri... uris) {
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        for (Uri uri : uris) {
            if (uri != null) {
                uriArrayList.add(uri);
            }
        }
        return uriArrayList;
    }

    /*OLD*/
    private static Bitmap resizeImageBitmap(Bitmap bitmap, int newSize) {
        int realWidth = bitmap.getWidth();
        int realHeight = bitmap.getHeight();

        int newWidth = 0;
        int newHeight = 0;

        if (realWidth > realHeight) {
            newWidth = newSize;
            newHeight = (newSize * realHeight) / realWidth;
        } else if (realWidth < realHeight) {
            newHeight = newSize;
            newWidth = (newSize * realWidth) / realHeight;
        } else if (realWidth == realHeight) {
            newHeight = newSize;
            newWidth = newSize;
        }

        float scaleWidth = ((float) newWidth) / realWidth;
        float scaleHeight = ((float) newHeight) / realHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, realWidth, realHeight, matrix, true);
    }

    public Bitmap getCompressedBitmap(String imagePath) {
        float maxHeight = 1920.0f;
        float maxWidth = 1080.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);

        byte[] byteArray = out.toByteArray();

        Bitmap updatedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return updatedBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
    /*OLD*/

    public void chooseGetImageDialog(Activity activity, ImageView imageView) {
        this.imageView = imageView;

        final String[] items = {"Pilih foto dari kamera", "Pilih foto dari galeri"};

        AlertDialog.Builder chooseImageDialog = new AlertDialog.Builder(mActivity);
        chooseImageDialog.setItems(items, (dialogInterface, i) -> {
            if (items[i].equals("Pilih foto dari kamera")) {
                openCamera(activity);
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
                Glide.with(mActivity).load(mCurrentPhotoPath).into(imageView);

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
                Snackbar.make(mActivity.findViewById(android.R.id.content), mActivity.getString(R.string.message_picture_failed), Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) { //FROM GALLERY
            try {
                Uri uri = data.getData();
                String galleryPath = getRealPathFromURIPath(uri);
                File file = new File(Uri.parse(galleryPath).getPath());
                try {
                    File compressedImage = new Compressor(mActivity).setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(file);
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

    private void openCamera(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(activity, false);
                mCurrentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(R.string.message_image_get_failed), Snackbar.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", photoFile));
                mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        mActivity.startActivityForResult(Intent.createChooser(intent, "Pilih foto"), REQUEST_IMAGE_GALLERY);
    }

    private File createImageFile(Activity activity, Boolean isUriFormat) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = activity.getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if (isUriFormat) {
            mCurrentPhotoPath = /*"file:" + */file.getAbsolutePath();
        } else {
            mCurrentPhotoPath = "file:" + file.getAbsolutePath();
        }
        return file;
    }

    private void compressImage() {
        File file = new File(Uri.parse(mCurrentPhotoPath).getPath());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap realImage = BitmapFactory.decodeStream(is);

        /*int realWidth = (int) (realImage.getWidth() * 0.8);
        int realHeight = (int) (realImage.getHeight() * 0.8);
        float ratio = Math.min((float) realWidth / realImage.getWidth(), (float) realHeight / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap original = Bitmap.createScaledBitmap(realImage, width, height, true);*/
        Bitmap original = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 1440, 1440, true);
//        Bitmap original = resizeImageBitmap(realImage, 720);
//        Bitmap original = getCompressedBitmap(mCurrentPhotoPath);

        try {
            ExifInterface ei = new ExifInterface(Uri.parse(mCurrentPhotoPath).getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

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
            original.compress(Bitmap.CompressFormat.JPEG, 100, stream);
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
