package com.kanzankazu.itungitungan.util.Firebase

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil
import java.io.File
import java.util.*

object FirebaseStorageUtil {

    private val rootRef: StorageReference
        get() = FirebaseStorage.getInstance().reference

    fun convertPathToUri(list: MutableList<String>): ArrayList<Uri> {
        /*int i = 0;
        while (i < list.size()) {

            i++;
        }*/
        val strings = ArrayList<Uri>()
        for (i in list.indices) {
            if (!InputValidUtil.isLinkUrl(list[i])) {
                strings.add(Uri.fromFile(File(list[i])))
            }
        }
        return strings
    }

    /*Upload*/
    fun uploadImages(mActivity: Activity, STORAGE_PATH: String, uris: MutableList<Uri>, listener: DoneListenerMultiple) {
        val imageDonwloadUrls = ArrayList<String>()
        val listStat = ArrayList<String>()
        for (i in uris.indices) {
            val uri = uris[i]

            if (uri != null) {
                val progressDialog = ProgressDialog(mActivity)
                progressDialog.setTitle("Menyimpan Gambar")
                progressDialog.show()

                val reference = rootRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + "jpg")
                reference.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        progressDialog.dismiss()
                        val imageDownloadUrl = taskSnapshot.downloadUrl!!.toString()
                        imageDonwloadUrls.add(imageDownloadUrl)
                        listStat.add("1")

                        val pathFromUri = PictureUtil.getImagePathFromUri(mActivity, uri)
                        PictureUtil.removeImageFromPathFile(pathFromUri)

                        val frequency0 = Collections.frequency(listStat, "0")
                        val frequency1 = Collections.frequency(listStat, "1")

                        if (frequency1 == uris.size) {
                            listener.isFinised(imageDonwloadUrls)
                        }
                    }
                    .addOnFailureListener { exception ->
                        progressDialog.dismiss()
                        listStat.add("0")

                        val frequency0 = Collections.frequency(listStat, "0")
                        val frequency1 = Collections.frequency(listStat, "1")

                        if (frequency0 == uris.size) {
                            listener.isFailed(exception.message!!)
                        }
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
                    }
            }
        }
    }

    fun uploadImage(mActivity: Activity, STORAGE_PATH: String, uri: Uri, listener: DoneListenerSingle) {
        val progressDialog = ProgressDialog(mActivity)
        progressDialog.setTitle("Menyimpan Gambar")
        progressDialog.show()

        val reference = rootRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + "jpg")
        reference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                val imageDownloadUrl = taskSnapshot.downloadUrl!!.toString()
                listener.isFinised(imageDownloadUrl)
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                listener.isFailed(exception.message!!)
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
            }
    }

    /*Delete*/
    fun deleteImages(mActivity: Activity, urls: MutableList<String>, listener: DoneRemoveListenerMultiple) {
        val listStat = ArrayList<String>()
        for (i in urls.indices) {
            val url = urls[i]

            val progressDialog = ProgressDialog(mActivity)
            progressDialog.setTitle("Menghapus Gambar")
            progressDialog.show()

            val reference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
            reference.delete()
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    listStat.add("1")

                    val frequency0 = Collections.frequency(listStat, "0")
                    val frequency1 = Collections.frequency(listStat, "1")

                    if (frequency1 == urls.size) {
                        listener.isFinised()
                    }
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    listStat.add("0")

                    val frequency0 = Collections.frequency(listStat, "0")
                    val frequency1 = Collections.frequency(listStat, "1")

                    if (frequency0 == urls.size) {
                        listener.isFailed(exception.message!!)
                    }
                }
        }
    }

    fun deleteImage(mActivity: Activity, url: String, listener: DoneRemoveListenerSingle) {
        val progressDialog = ProgressDialog(mActivity)
        progressDialog.setTitle("Menghapus Gambar")
        progressDialog.show()

        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        reference.delete()
            .addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                listener.isFinised()

            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                listener.isFailed(exception.message!!)

            }
    }

    /*interface*/
    interface DoneListenerMultiple {
        fun isFinised(imageDownloadUrls: ArrayList<String>)

        fun isFailed(message: String)
    }

    interface DoneListenerSingle {
        fun isFinised(imageDownloadUrl: String)

        fun isFailed(message: String)
    }

    interface DoneRemoveListenerMultiple {
        fun isFinised()

        fun isFailed(message: String)
    }

    interface DoneRemoveListenerSingle {
        fun isFinised()

        fun isFailed(message: String)
    }
}
