package com.kanzankazu.itungitungan.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil;
import com.kanzankazu.itungitungan.util.widget.CompressBitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class PictureUtil {

    interface PhotoCodeEnum {

        int CAMERA_CODE = 1;
        int GALLERY_CODE = 2;
    }

    public static String mCurrentPhotoPath;
    public static int REQUEST_IMAGE_CAPTURE = 21;
    public static int RESULT_LOAD_IMAGE = 1;

    public static Intent getCropperIntent(Context mContext, Uri imageUri, @Nullable Integer sizex, @Nullable Integer sizey) {
        Intent CropIntent = null;

        if (sizex == null) {
            sizex = 800;
        }
        if (sizey == null) {
            sizey = 900;
        }
        if (sizex > 1500) {
            sizex = 1500;
        }
        if (sizey > 1500) {
            sizey = 1500;
        }

        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(imageUri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", sizex);
            CropIntent.putExtra("outputY", sizey);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            return CropIntent;
        } catch (ActivityNotFoundException e) {
            return CropIntent;
        }
    }

    public static Bitmap resizeImage(Bitmap bitmap, int newSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = 0;
        int newHeight = 0;

        if (width > height) {
            newWidth = newSize;
            newHeight = (newSize * height) / width;
        } else if (width < height) {
            newHeight = newSize;
            newWidth = (newSize * width) / height;
        } else if (width == height) {
            newHeight = newSize;
            newWidth = newSize;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Uri getUriFromResult(Context context, int resultCode, Uri jink) {
        InputStream in = null;
        Uri selectedImage = null;
        if (resultCode == RESULT_OK) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), jink);
                in = context.getContentResolver().openInputStream(jink);
                ExifInterface exifInterface = new ExifInterface(in);

                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        selectedImage = rotateImage(bitmap, 90, context);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        selectedImage = rotateImage(bitmap, 180, context);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        selectedImage = rotateImage(bitmap, 270, context);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        selectedImage = jink;
                        break;
                }

            } catch (IOException e) {
                // Handle any errors
                return null;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {
                    }
                }
                return selectedImage;
            }
        } else {
            return selectedImage;
        }
    }

    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), "Tempe");
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    public static String bitmapToBase64(Bitmap bitmap, Bitmap.CompressFormat format, int compression) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, compression, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP);
        }
    }

    public static Bitmap base64ToBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64.replace("data:image/jpeg;base64,", ""), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Uri rotateImage(Bitmap source, float angle, Context mContext) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap temp = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        temp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), temp, "Title", null);
        return Uri.parse(path);

    }

    public static Bitmap rotateImage(Bitmap source, float angle, Context mContext, Bitmap.CompressFormat format, int compressionLevel) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap temp = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        temp.compress(format, compressionLevel, bytes);
        return temp;
    }

    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static void compressImage(String mCurrentPhotoPath) {
        File file = new File(Uri.parse(mCurrentPhotoPath).getPath());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap original = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream), 1440, 1440, true);

        try {
            ExifInterface ei = new ExifInterface(Uri.parse(mCurrentPhotoPath).getPath());
            int orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
            );

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            original.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean requestCameraAndStoragePermissions(Activity activity) {
        AndroidPermissionUtil permissionUtil = new AndroidPermissionUtil(activity,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionUtil.checkPermission(permissionUtil.getCurrentpermission());
    }

    /*public static void onActivityResult(int requestCode, int resultCode, Intent data, Activity mActivity, String currentPhotoPath, ImageView targeView) {
        if (requestCode == PhotoCodeEnum.CAMERA_CODE && resultCode == Activity.RESULT_OK && TextUtils.isEmpty(currentPhotoPath)) {
            try {
                compressImage(currentPhotoPath);
                currentPhotoPath = getRealPathFromURIPath(Uri.parse(currentPhotoPath), mActivity);

                Glide.with(mActivity).load(currentPhotoPath).into(targeView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PhotoCodeEnum.GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {
            File file = new File(Uri.parse(getRealPathFromURIPath(data.getData(), mActivity)).getPath());
            try {
                File compressedImage = new Compressor(mActivity)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(file);

                currentPhotoPath = getRealPathFromURIPath(Uri.parse(compressedImage.getAbsolutePath()), mActivity);

                Glide.with(mActivity).load(currentPhotoPath).into(targeView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setupChooseImageDialog(Activity mActivity) {
        //ArrayList<String> items = new ArrayList<>(Arrays.asList("Pilih foto dari kamera", "Pilih foto dari galeri"));
        String[] strings = {"Pilih foto dari kamera", "Pilih foto dari galeri"};
        AlertDialog.Builder chooseImageDialog = new AlertDialog.Builder(mActivity);
        chooseImageDialog.setItems(strings, (dialog, which) -> {
            if (strings[which].equalsIgnoreCase("Pilih foto dari kamera")) getImageFromCamera(mActivity);
            else getImageFromGallery(mActivity);
        });
        chooseImageDialog.show();
    }

    public static void getImageFromCamera(Activity mActivity) {
        if (requestCameraAndStoragePermissions(mActivity)) {
            Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (intentPhoto.resolveActivity(mActivity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    String currentPhotoPath = "file:" + photoFile.getAbsolutePath();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24)
                    intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", photoFile));
                else
                    intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                mActivity.startActivityForResult(intentPhoto, PhotoCodeEnum.CAMERA_CODE);
            }
        }
    }

    public static void getImageFromGallery(Activity mActivity) {
        if (requestCameraAndStoragePermissions(mActivity)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            mActivity.startActivityForResult(Intent.createChooser(intent, "Pilih foto"), PhotoCodeEnum.GALLERY_CODE);
        }
    }*/

    public static void chooseImageDialog(AppCompatActivity mActivity) {
        final String[] items = {"Pilih foto dari kamera", "Pilih foto dari galeri"};

        AlertDialog.Builder chooseImageDialog = new AlertDialog.Builder(mActivity);
        chooseImageDialog.setItems(items, (dialogInterface, i) -> {
            if (items[i].equals("Pilih foto dari kamera")) {
                takePicture(mActivity);
            } else {
                checkPermission(mActivity);
            }
        });

        chooseImageDialog.show();
    }

    public static void checkPermission(Activity mActivity) {
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            System.out.println("permission granted");
            getPicture(mActivity);

        } else {
            System.out.println("permission deny");
            requestPermission(mActivity);
        }
    }

    public static void requestPermission(Activity mActivity) {
        int REQUEST_PERMISSION_EXTERNAL_STORAGE = 2;
        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
    }

    public static void getPicture(Activity mActivity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        mActivity.startActivityForResult(Intent.createChooser(intent, "Pilih foto"), RESULT_LOAD_IMAGE);
    }

    public static void takePicture(AppCompatActivity mActivity) {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(mActivity);
        }
    }

    public static void dispatchTakePictureIntent(AppCompatActivity mActivity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                        mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    public static String onActivityResult(int requestCode, int resultCode, Intent data, AppCompatActivity mActivity) {
        System.out.println("data on activity result : " + data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && mCurrentPhotoPath != null) { //FROM CAMERA
            try {
                compressImage();

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
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) { //FROM GALLERY
            try {
                Uri uri = data.getData();

                CompressBitmap cb = new CompressBitmap(mActivity);
                String filepath = cb.getRealPathFromURI(mActivity, data.getData());
                Bitmap bitmap = cb.compressImage(cb.getRealPathFromURI(mActivity, data.getData()), filepath);

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
        }
        return "";
    }

    public static void compressImage() {
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(), matrix, true
        );
    }


}
