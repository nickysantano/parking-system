package id.parking.core.firestore

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.parking.core.other.Resource

//crud database firebase
class FDBApp {

    companion object {
        fun init(context: Context) {
            FirebaseApp.initializeApp(context)
        }

        inline fun <reified T> insert(docId: (String) -> Unit): Inserter<T> {
            val table = T::class.java.simpleName
            val newId = Firebase.firestore.collection(table).document().id
            docId(newId)
            return Inserter(newId)
        }

        fun <T> update(docId: String, data: T): LiveData<Resource<String>> {
            val table = data!!::class.java.simpleName
            val listener = MutableLiveData<Resource<String>>()
            listener.postValue(Resource.loading())
            Firebase.firestore.collection(table)
                .document(docId)
                .set(data)
                .addOnSuccessListener {
                    listener.postValue(Resource.success("1"))
                }
                .addOnFailureListener { e ->
                    listener.postValue(Resource.error(e.message))
                }
            return listener
        }

        inline fun <reified T> getAll(): LiveData<Resource<List<T>>> {
            val table = T::class.java.simpleName
            val listener = MutableLiveData<Resource<List<T>>>()
            listener.postValue(Resource.loading())
            Firebase.firestore.collection(table).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val list = document.toObjects(T::class.java)
                        listener.postValue(Resource.success(list))
                    } else {
                        listener.postValue(Resource.error("Data null"))
                    }
                }
                .addOnFailureListener { e ->
                    listener.postValue(Resource.error(e.message))
                }
            return listener
        }

        inline fun <reified T> get(docId: String): LiveData<Resource<T>> {
            val table = T::class.java.simpleName
            val listener = MutableLiveData<Resource<T>>()
            listener.postValue(Resource.loading())
            Firebase.firestore.collection(table).document(docId)
                .get()
                .addOnSuccessListener { doc ->
                    doc.toObject(T::class.java)?.let {
                        listener.postValue(Resource.success(it))
                    } ?: listener.postValue(Resource.error("Data null"))
                }
                .addOnFailureListener { e ->
                    listener.postValue(Resource.error(e.message))
                }
            return listener
        }

        inline fun <reified T> delete(docId: String): LiveData<Resource<String>> {
            val table = T::class.java.simpleName
            val listener = MutableLiveData<Resource<String>>()
            listener.postValue(Resource.loading())
            Firebase.firestore.collection(table)
                .document(docId)
                .delete()
                .addOnSuccessListener {
                    listener.postValue(Resource.success("1"))
                }
                .addOnFailureListener { e ->
                    listener.postValue(Resource.error(e.message))
                }
            return listener
        }

    }
}