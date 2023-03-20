package id.parking.core.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.parking.core.other.Resource

//helper insert atau create pada firebase
class Inserter<T>(private var newId: String) {

    fun insert(data: T): LiveData<Resource<String>> {
        val listener = MutableLiveData<Resource<String>>()
        listener.postValue(Resource.loading())
        Firebase.firestore.collection(data!!::class.java.simpleName)
            .document(newId)
            .set(data)
            .addOnSuccessListener {
                listener.postValue(Resource.success("1"))
            }
            .addOnFailureListener { e ->
                listener.postValue(Resource.error(e.message))
            }
        return listener
    }

}