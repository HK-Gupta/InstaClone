package com.example.instaclone.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// In this Function callback method is used to return the string
// so that the value is passed only if the task completed.
fun uploadImage(uri: Uri, folderName: String, callback:(String?)->Unit){
    var imageUrl:String? = null
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString()) // UUID is giving the random string value to store the image.
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
}

fun uploadVideo(uri: Uri, folderName: String, progressDialog: ProgressDialog, callback:(String?)->Unit){
    var videoUrl:String? = null
    progressDialog.setTitle("Uploading...")
    progressDialog.show()
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString()) // UUID is giving the random string value to store the image.
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                videoUrl = it.toString()
                progressDialog.dismiss()
                callback(videoUrl)
            }
        }.addOnProgressListener {
            val uploadedValue:Long = (it.bytesTransferred/it.totalByteCount)*100
            progressDialog.setMessage("Uploaded $uploadedValue %")
        }
}