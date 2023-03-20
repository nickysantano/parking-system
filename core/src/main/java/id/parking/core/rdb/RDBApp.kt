package id.parking.core.rdb

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import id.parking.core.other.Resource

class RDBApp {

    companion object {
        fun init(context: Context) {
            FirebaseApp.initializeApp(context)
        }
        
        private fun lockRef(): DatabaseReference {
            return FirebaseDatabase.getInstance().reference
                .child("lock")
        }

        private fun childStatus(id: String): DatabaseReference {
            return lockRef()
                .child(id)
                .child("status")
        }

        private fun childLocked(id: String): DatabaseReference {
            return lockRef()
                .child(id)
                .child("locked")
        }

        fun setStatus(id: String, status: String): LiveData<Resource<String>> {
            val listener = MutableLiveData<Resource<String>>()
            listener.postValue(Resource.loading())
            childStatus(id).setValue(status)
                .addOnSuccessListener {
                    listener.postValue(Resource.success("1"))
                }.addOnFailureListener {
                    listener.postValue(Resource.error(it.message))
                }
            return listener
        }

        fun setLocked(id: String, locked: Int): LiveData<Resource<String>> {
            val listener = MutableLiveData<Resource<String>>()
            listener.postValue(Resource.loading())
            childLocked(id).setValue(locked)
                .addOnSuccessListener {
                    listener.postValue(Resource.success("1"))
                }.addOnFailureListener {
                    listener.postValue(Resource.error(it.message))
                }
            return listener
        }

        fun getId(id: String): LiveData<Resource<Boolean>> {
            val listener = MutableLiveData<Resource<Boolean>>()
            listener.postValue(Resource.loading())
            lockRef()
                .child(id)
                .get().addOnSuccessListener {
                    listener.postValue(Resource.success(it.exists()))
                }.addOnFailureListener {
                    listener.postValue(Resource.error(it.message))
                }
            return listener
        }

        fun getLock(id: String): LiveData<Resource<LockData>> {
            val listener = MutableLiveData<Resource<LockData>>()
            listener.postValue(Resource.loading())
            lockRef()
                .child(id)
                .get().addOnSuccessListener {
                    it.getValue(LockData::class.java)?.let { lock ->
                        listener.postValue(Resource.success(lock))
                    } ?: kotlin.run {
                        listener.postValue(Resource.error("lock null"))
                    }
                }.addOnFailureListener {
                    listener.postValue(Resource.error(it.message))
                }
            return listener
        }

        fun setLock(id: String, lockData: LockData): LiveData<Resource<String>> {
            val listener = MutableLiveData<Resource<String>>()
            listener.postValue(Resource.loading())
            lockRef()
                .child(id)
                .setValue(lockData).addOnSuccessListener {
                    listener.postValue(Resource.success("1"))
                }.addOnFailureListener {
                    listener.postValue(Resource.error(it.message))
                }
            return listener
        }

        fun listenLock(id: String): LiveData<Resource<LockData>> {
            val listener = MutableLiveData<Resource<LockData>>()
            lockRef().child(id).addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(LockData::class.java)?.let { lock ->
                        listener.postValue(Resource.success(lock))
                    } ?: kotlin.run {
                        listener.postValue(Resource.error("lock data null"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.postValue(Resource.error(error.message))
                }
            })
            return listener
        }

    }
}