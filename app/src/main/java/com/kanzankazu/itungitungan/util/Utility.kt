package id.otomoto.otr.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.fido.fido2.api.common.RequestOptions
import com.google.gson.Gson
import com.kanzankazu.itungitungan.R
import kotlinx.android.synthetic.main.layout_confirmation_dialog.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import java.io.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

/**
 * Created by Stefanus Candra on 13/08/2019.
 */
object Utility {
    interface DialogButtonListener {
        fun onDialogButtonClick()
    }

    fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "Failed to convert request body"
        }
    }

    fun createPlainTextRequestBody(plainText: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), plainText)
    }

    fun closeSoftKeyboard(activity: Activity) {
        val im = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        im.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun compressImage(mCurrentPhotoPath: String) {
        val file = File(Uri.parse(mCurrentPhotoPath).path!!)
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        var original =
            Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream), 1440, 1440, true)

        try {
            val ei = ExifInterface(Uri.parse(mCurrentPhotoPath).path)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> original = rotateImage(original, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> original = rotateImage(original, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> original = rotateImage(original, 270f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val stream = FileOutputStream(file)
            original.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }
    }

    fun convertDpToPixel(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.roundToInt()
    }

    fun convertMapObjectToJsonObject(map: Map<String, Any>, printTag: String) {
        val gson = Gson()
        println("Gson to Json Object " + printTag + " : " + gson.toJsonTree(map).asJsonObject)
    }

    fun convertObjectToJson(tag: String, obj: Any) {
        val gson = Gson()
        val jsonObject = gson.toJson(obj)
        println(tag + jsonObject)
    }

    fun convertToCharSequenceArray(array: List<CharSequence>): Array<CharSequence> {
        return array.toTypedArray()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    fun fromHtml(htmlSource: String?): Spanned {
        return if (htmlSource != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //FROM_HTML_MODE_LEGACY ->Separate block-level elements with blank lines
                Html.fromHtml(htmlSource, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(htmlSource)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml("", Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml("")
            }
        }
    }

    fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String? {
        val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            return contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }



    fun intentWithClearTask(activity: AppCompatActivity?, classDestination: Class<*>) {
        if (activity != null) {
            val intent = Intent(activity, classDestination)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.overridePendingTransition(0, 0)
            activity!!.startActivity(intent)
            activity!!.finish()
        }
    }

    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses
        for (proccessInfo in procInfos) {
            if (proccessInfo.processName == packageName)
                return true
        }
        return false
    }

    fun openUrlWithChromeCustomTabs(url: String, context: Context) {
        val uri = Uri.parse(url)
        val intentBuilder = CustomTabsIntent.Builder()

        // customizing toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.navy_blue))
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.navy_blue))

        // set start and exit animations
        intentBuilder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
        intentBuilder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)

        // build custom tabs intent and launch the url
        val customTabsIntent = intentBuilder.build()
        customTabsIntent.launchUrl(context, uri)
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun requestCameraAndStoragePermissions(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val REQUEST_CODE = 11
            val appPermission = arrayListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)

            val neededPermission: ArrayList<String> = ArrayList()
            for (permission in appPermission) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    neededPermission.add(permission)
                }
            }

            if (neededPermission.isNotEmpty()) {
                val array = arrayOfNulls<String>(neededPermission.size)
                ActivityCompat.requestPermissions(
                    activity,
                    neededPermission.toArray(array),
                    REQUEST_CODE
                )
                return false
            }
        }
        return true
    }

    fun requestWriteExternalStoragePermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("Permission", "Permission to Write External Storage Granted")
            } else {
                Log.i("Permission", "Permission to Write External Storage Revoked")
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    11
                )
            }
        } else {
            Log.i("Permission", "Permission Write External Storage Granted")
        }
    }

    fun requestReadExternalStoragePermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission to Read External Storage Granted")
            } else {
                Log.i("Permission", "Permission to Read External Storage Revoked")
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    12
                )
            }
        } else {
            Log.i("Permission", "Permissions are granted")
        }
    }

    fun requestCameraPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission to Access Camera Granted")
            } else {
                Log.i("Permission", "Permission to Access Camera Revoked")
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CAMERA),
                    13
                )
            }
        } else {
            Log.i("Permission", "Permission to Access Camera Granted")
        }
    }

    fun setupToolbarForActivity(mActivity: AppCompatActivity, toolbar: Toolbar, title: String) {
        val tvTitleToolbar = mActivity.findViewById<TextView>(R.id.tv_title_toolbar)
        tvTitleToolbar.text = title

        mActivity.setSupportActionBar(toolbar)
        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        mActivity.supportActionBar?.title = ""

        toolbar.setNavigationOnClickListener { mActivity.onBackPressed() }
    }

    fun showCalendarDialog(activity: Activity, viewTarget: EditText): Calendar {
        lateinit var datePickerDialog: DatePickerDialog
        val calendar: Calendar = Calendar.getInstance()
        val myDateListener = DatePickerDialog.OnDateSetListener { picker, year, month, date ->
            val tag = picker.tag as Int
            if (tag == viewTarget.id) {
                calendar.set(year, month, date)
                DateTimeUtils.setInDateFormalFormat(calendar, viewTarget)
            }
        }
        datePickerDialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog(activity, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        } else {
            DatePickerDialog(activity, android.app.AlertDialog.THEME_HOLO_LIGHT, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
        datePickerDialog.datePicker.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        datePickerDialog.datePicker.tag = viewTarget.id
        datePickerDialog.show()
        return calendar
    }

    fun showConfirmationDialog(activity: Activity, dialogButtonListener: DialogButtonListener, dialogTitle: String, dialogDescription: String) {
        if (!activity.isFinishing) {
            val dialog = AlertDialog.Builder(activity)
                .setView(activity.layoutInflater.inflate(R.layout.layout_confirmation_dialog, null))
                .show()
            val tvConfirmationDialogTitle = dialog.tv_confirmation_text
            val tvConfirmationDialogDescription = dialog.tv_confirmation_description
            val tvCloseDialog = dialog.btn_close_dialog
            val tvCloseDialogOk = dialog.btn_close_dialog_OK
            //Text and visibility
            tvConfirmationDialogTitle.visibility = if (dialogTitle.isEmpty()) View.GONE else View.VISIBLE
            tvConfirmationDialogTitle.text = dialogTitle
            tvConfirmationDialogDescription.visibility = if (dialogDescription.isEmpty()) View.GONE else View.VISIBLE
            tvConfirmationDialogDescription.text = dialogDescription
            //Action
            tvCloseDialog.setOnClickListener { dialog.dismiss() }
            tvCloseDialogOk.setOnClickListener {
                dialogButtonListener.onDialogButtonClick()
                dialog.dismiss()
            }
        }
    }

    @SuppressLint("InflateParams")
    fun showDisclaimerDialog(activity: Activity, title: String, content: String) {
        if (!activity.isFinishing) {
            val dialog = AlertDialog.Builder(activity)
                .setView(activity.layoutInflater.inflate(R.layout.layout_dialog_disclaimer, null))
                .setCancelable(false)
                .show()

            val tvTitleDialog = dialog.findViewById<TextView>(R.id.tv_disclaimer_title)
            val tvDescDialog = dialog.findViewById<TextView>(R.id.tv_disclaimer_description)
            val btnDialog = dialog.findViewById<TextView>(R.id.btn_close_dialog)

            tvTitleDialog!!.text = title
            tvDescDialog!!.text = content
            btnDialog!!.text = "OK"
            btnDialog.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    @SuppressLint("InflateParams")
    fun showDisclaimerDialog(activity: Activity, title: String, content: String, dialogButtonListener: DialogButtonListener) {
        if (!activity.isFinishing) {
            val dialog = AlertDialog.Builder(activity)
                .setView(activity.layoutInflater.inflate(R.layout.layout_dialog_disclaimer, null))
                .setCancelable(false)
                .show()

            val tvTitleDialog = dialog.findViewById<TextView>(R.id.tv_disclaimer_title)
            val tvDescDialog = dialog.findViewById<TextView>(R.id.tv_disclaimer_description)
            val btnDialog = dialog.findViewById<TextView>(R.id.btn_close_dialog)

            tvTitleDialog!!.text = title
            tvDescDialog!!.text = content
            btnDialog!!.text = "OK"
            btnDialog.setOnClickListener {
                dialogButtonListener.onDialogButtonClick()
                dialog.dismiss()
            }
        }
    }

    @SuppressLint("InflateParams")
    fun showRetryDialog(activity: Activity, dialogButtonListener: DialogButtonListener) {
        if (!activity.isFinishing) {
            val dialog = AlertDialog.Builder(activity)
                .setView(activity.layoutInflater.inflate(R.layout.layout_dialog_single_button, null))
                .setCancelable(false)
                .show()

            val ivDialog = dialog.findViewById<ImageView>(R.id.iv_single_button_dialog)
            val tvDescDialog = dialog.findViewById<TextView>(R.id.tv_single_button_dialog)
            val btnDialog = dialog.findViewById<TextView>(R.id.btn_close_dialog)

            tvDescDialog!!.text = "Gagal terhubung jaringan,\n Silahkan coba kembali."
            btnDialog!!.text = "Coba Lagi"
            btnDialog.setOnClickListener {
                dialogButtonListener.onDialogButtonClick()
                dialog.dismiss()
            }
        }
    }

    @SuppressLint("InflateParams")
    fun showRetryDialog(activity: Activity, message: String, image: Int, btnMessage: String, dialogButtonListener: DialogButtonListener) {
        if (!activity.isFinishing) {
            val dialog = AlertDialog.Builder(activity)
                .setView(activity.layoutInflater.inflate(R.layout.layout_dialog_single_button, null))
                .setCancelable(false)
                .show()

            val ivDialog = dialog.findViewById<ImageView>(R.id.iv_single_button_dialog)
            val tvDescDialog = dialog.findViewById<TextView>(R.id.tv_single_button_dialog)
            val btnDialog = dialog.findViewById<TextView>(R.id.btn_close_dialog)

            ivDialog!!.setImageResource(image)
            tvDescDialog!!.text = message
            btnDialog!!.text = btnMessage
            btnDialog.setOnClickListener {
                dialogButtonListener.onDialogButtonClick()
                dialog.dismiss()
            }
        }
    }

    fun showSnackbar(view: View, message: String) {
        try {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val sbView = snackbar.view
            val textView =
                sbView.findViewById<TextView>(R.id.snackbar_text)
            textView.setTextColor(Color.WHITE)
            textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            snackbar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setRupiah(inputCredit: String): String? {
        var sCredit: String? = null
        val credit: Int
        try {
            if (inputCredit.contains(".")) {
                sCredit = inputCredit.substring(0, inputCredit.indexOf("."))
                credit = Integer.parseInt(sCredit)
                sCredit =
                    "Rp " + NumberFormat.getNumberInstance(Locale.US).format(credit.toLong()).replace(',', '.')
            } else {
                credit = Integer.parseInt(inputCredit)
                sCredit =
                    "Rp " + NumberFormat.getNumberInstance(Locale.US).format(credit.toLong()).replace(',', '.')
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sCredit
    }

    fun setNoRupiah(inputCredit: String): String {
        return inputCredit.replace("Rp ".toRegex(), "").replace("\\.".toRegex(), "")
    }

}