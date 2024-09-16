package com.example.inovasi.repository

import com.example.inovasi.model.AccountModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AccountRepository {

    private val db = FirebaseFirestore.getInstance()

    fun save(accountModel : AccountModel) {
        val accountRef = db.collection("account").document(accountModel.id)
        accountRef.set(accountModel)
    }

    suspend fun get(id : String, listener : EventListener<DocumentSnapshot>? = null) : AccountModel? {

        val accountRef = db.collection("account").document(id)
        listener?.let {
            accountRef.addSnapshotListener(listener)
        }

        val document = accountRef.get().await()

        return document.toObject(AccountModel::class.java)
    }
}