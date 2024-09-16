package com.example.inovasi.model

import com.google.firebase.firestore.Blob

data class FileModel(
    val title : String = "",
    val data : Blob = Blob.fromBytes(ByteArray(1))
)