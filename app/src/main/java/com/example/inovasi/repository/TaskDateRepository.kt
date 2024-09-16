package com.example.inovasi.repository

import com.example.inovasi.model.TaskDateModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class TaskDateRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getAll(userId : String, listener : EventListener<QuerySnapshot>? = null) : List<TaskDateModel> {

        val returnValue = ArrayList<TaskDateModel>()
        val ref = db
            .collection("account/$userId/tasks")
            .orderBy("date")

        val documents = ref.get().await()

        val calendar: Calendar = Calendar.getInstance()

        for (document in documents) {
            val date = document.getDate("date")
            date?.let {
                calendar.setTime(date)

                val year: Int = calendar.get(Calendar.YEAR)
                val month: Int = calendar.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar
                val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

                returnValue.add(
                    TaskDateModel(year, month, day)
                )
            }
        }

        listener?.let {
            ref.addSnapshotListener(it)
        }

        return returnValue
    }
}