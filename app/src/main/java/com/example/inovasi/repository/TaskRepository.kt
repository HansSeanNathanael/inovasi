package com.example.inovasi.repository

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.inovasi.model.TaskModel
import com.example.inovasi.service.SendFirebaseMessage
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.util.Date

class TaskRepository {

    private val db = FirebaseFirestore.getInstance()

    fun save(context : Context, userId : String, taskModel : TaskModel) {
        if (taskModel.status == "pending") {
            SendFirebaseMessage().send(context, taskModel)
        }

        val taskRef = db.collection("account/$userId/tasks").document(taskModel.id)
        taskRef.set(taskModel)

    }

    suspend fun getAll(userId : String, listener : EventListener<QuerySnapshot>? = null) : List<TaskModel> {

        val returnValue = ArrayList<TaskModel>()
        val ref = db
            .collection("account/$userId/tasks")
            .orderBy("date")

        val documents = ref.get().await()

        for (document in documents) {
            val taskModel = document.toObject(TaskModel::class.java)
            returnValue.add(taskModel)
        }

        listener?.let {
            ref.addSnapshotListener(it)
        }

        return returnValue
    }

    suspend fun getAllWithStatus(userId : String, status : String, listener : EventListener<QuerySnapshot>? = null) : List<TaskModel> {

        val returnValue = ArrayList<TaskModel>()
        val ref = db
            .collection("account/$userId/tasks")
            .orderBy("date")

        val documents = ref.get().await()

        for (document in documents) {
            val taskModel = document.toObject(TaskModel::class.java)
            if (taskModel.status == status) {
                returnValue.add(taskModel)
            }
        }

        listener?.let {
            ref.addSnapshotListener(it)
        }

        return returnValue
    }

    suspend fun getById(userId : String, documentId : String, listener : EventListener<DocumentSnapshot>? = null) : TaskModel? {

        val returnValue = ArrayList<TaskModel>()
        val ref = db
            .collection("account/$userId/tasks").document(documentId)

        val document = ref.get().await()

        listener?.let {
            ref.addSnapshotListener(it)
        }

        return document.toObject(TaskModel::class.java)
    }

    suspend fun getBy(userId : String, status: String? = null, startTime : Date? = null, endTime: Date? = null, listener : EventListener<QuerySnapshot>? = null) : List<TaskModel> {

        val returnValue = ArrayList<TaskModel>()
        var ref = db
            .collection("account/$userId/tasks")
            .orderBy("date")


        val documents = ref.get().await()

        for (document in documents) {
            val taskModel = document.toObject(TaskModel::class.java)

            if (status != null && taskModel.status != status) {
                continue
            }
            if (startTime != null && taskModel.date.before(startTime)) {
                continue
            }
            if (endTime != null && taskModel.date.after(endTime)) {
                continue
            }

            returnValue.add(taskModel)
        }

        listener?.let {
            ref.addSnapshotListener(it)
        }

        return returnValue
    }
}