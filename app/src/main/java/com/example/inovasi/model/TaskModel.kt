package com.example.inovasi.model

import java.util.Date

data class TaskModel(
    val id : String = "",
    val subject : String = "",
    val description : String = "",
    val date : Date = Date(),
    val status : String = "",
    val reminder : Date = Date(),
    val fileData : List<FileModel> = ArrayList()
)