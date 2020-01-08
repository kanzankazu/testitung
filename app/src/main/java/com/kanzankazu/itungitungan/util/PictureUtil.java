package com.kanzankazu.itungitungan.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import android.util.Base64;
import android.util.Log;

import com.kanzankazu.itungitungan.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureUtil {

    static String PATH_FILE = "/." + BuildConfig.APP_NAME + "/";

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

    public static Bitmap resizeImageBitmap(Bitmap bitmap, int newSize) {
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
        InputStream inputStream = null;
        Uri selectedImage = null;
        if (resultCode == Activity.RESULT_OK) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), jink);
                inputStream = context.getContentResolver().openInputStream(jink);
                ExifInterface exifInterface = new ExifInterface(inputStream);

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
                Log.e("Lihat", "getUriFromResult PictureUtil : " + e.getMessage());
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("Lihat", "getUriFromResult PictureUtil : " + e.getMessage());
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

    public static Bitmap rotateBitmap(String src) {
        Bitmap bitmap = BitmapFactory.decodeFile(src);
        try {
            ExifInterface exif = new ExifInterface(src);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                case ExifInterface.ORIENTATION_UNDEFINED:
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getImagePathFromUri(Activity activity, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        activity.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getImagePathFromUri2(Activity activity, Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static Bitmap getImageBitmapFromPathFile(String imagePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        return bitmap;
    }

    public static Bitmap getImageBitmapFromPathFile1(String mCurrentPhotoPath) {
        Bitmap bitmap;
        Uri imageUri = Uri.parse(mCurrentPhotoPath);
        File file = new File(imageUri.getPath());
        try {
            InputStream ims = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(ims);
        } catch (FileNotFoundException e) {
            return null;
        }
        return bitmap;
    }

    public static Uri getImageUriFromBitmap(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Uri getImageUriFromPath(Activity activity, String path) {
        Bitmap bitmapFromPathFile = getImageBitmapFromPathFile(path);
        return getImageUriFromBitmap(activity, bitmapFromPathFile);
    }

    public static boolean removeImageFromPathFile(String pathImg) {
        File fdelete = new File(pathImg);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.d("Lihat", "removeImageFromPathFile PictureUtil : " + "file Deleted :" + pathImg);
                return true;
            } else {
                Log.d("Lihat", "removeImageFromPathFile PictureUtil : " + "file not Deleted :" + pathImg);
                return false;
            }
        }
        return false;
    }

    public static boolean removeImageFromPathFile2(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    public static Bitmap flipHorizontalImage(Bitmap src) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();

        matrix.preScale(-1.0f, 1.0f);

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap flipVerticalImage(Bitmap src) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();

        matrix.preScale(1.0f, -1.0f);

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap flipHorizontally(Bitmap originalImage) {
        // The gap we want between the flipped image and the original image
        final int flipGap = 4;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // This will not scale but will flip on the Y axis
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        // Create a Bitmap with the flip matrix applied to it.
        // We only want the bottom half of the image
        Bitmap flipImage = Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, true);

        // Create a new bitmap with same width but taller to fit reflection
        Bitmap bitmapWithFlip = Bitmap.createBitmap((width + width + flipGap), height, Bitmap.Config.ARGB_8888);

        /*// Create a new Canvas with the bitmap that's big enough for
        Canvas canvas = new Canvas(bitmapWithFlip);

        //Draw original image
        canvas.drawBitmap(originalImage, 0, 0, null);

        //Draw the Flipped Image
        canvas.drawBitmap(flipImage, width + flipGap, 0, null);*/

        return flipImage;
    }

    public static void onSelectGallery(Intent data) {
        /*Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        base64pic = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Log.d("bes64 ", base64pic);*/
    }

    public static void createDirectoryAndSaveFile(Context context, Bitmap image, String idName) {
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FILE + idName);
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + PATH_FILE + idName);
        if (storageDir.exists()) {
            storageDir.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(storageDir);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String saveImageWithFolder(Context context, Bitmap image, String idName, String folderName) {
        String savedImagePath = null;
        String imageFileName = idName + ".jpg";
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FILE + folderName);
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + PATH_FILE + folderName);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile, false);
                Bitmap storedata = PictureUtil.resizeImageBitmap(image, 360);
                storedata.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("Lihat", "saveImageWithFolder : " + savedImagePath);
        return savedImagePath;
    }

    public static String saveImageLogoBackIcon(Context context, Bitmap image, String idName) {
        String savedImagePath = null;
        String imageFileName = idName + ".jpg";
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FILE);
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + PATH_FILE);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile, false);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return savedImagePath;
    }

    public static String saveImageHomeContentImage(Activity context, Bitmap image, String idName, String eventId) {
        String savedImagePath = null;
        String imageFileName = idName + ".jpg";
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FILE + eventId);
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + PATH_FILE + eventId);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            if (!imageFile.exists()) {
                try {
                    OutputStream fOut = new FileOutputStream(imageFile, false);
                    Bitmap resizeImageBitmap = PictureUtil.resizeImageBitmap(image, 360);
                    resizeImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return savedImagePath;
    }

    public static String saveImageHomeContentIcon(Activity context, Bitmap image, String idName, String eventId) {
        String savedImagePath = null;
        String imageFileName = idName + ".jpg";
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FILE + eventId);
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + PATH_FILE + eventId);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile, false);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return savedImagePath;
    }

    public static String saveImageStaticMaps(Context context, Bitmap image, String idName) {
        String savedImagePath = null;
        String imageFileName = idName + ".jpg";
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FILE + "StaticMaps");
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + PATH_FILE + "StaticMaps");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile, false);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("Lihat", "saveImageStaticMaps : " + savedImagePath);
        return savedImagePath;
    }

    public static void downloadImageToPicture(Activity activity, Bitmap bitmap, String title, String id) {
        String savedImagePath;
        String replace = title.replace(" ", "");
        String imageFileName = replace + "_" + id + ".jpg";
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)/* + "/Openguide/" + id*/.toString());
        //File imageDir = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)/* + "/Openguide/" + id*/.toString());
        boolean success = true;
        if (!imageDir.exists()) {
            success = imageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(imageDir, imageFileName);
            try {
                OutputStream fileOutputStream = new FileOutputStream(imageFile, false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
                Utils.showToast(activity, "Save Image Success");
            } catch (Exception e) {
                e.printStackTrace();
            }
            savedImagePath = imageFile.getAbsolutePath();
            Log.d("Lihat", "downloadImageToPicture PictureUtil : " + savedImagePath);
        }
    }

    public static void deleteImageFile(Activity context, String eventIds) {
        //db.deleleDataByKey(SQLiteHelper.TableGallery, SQLiteHelper.Key_Gallery_eventId, eventIds);

        //File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FILE + eventIds);
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + PATH_FILE + eventIds);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
