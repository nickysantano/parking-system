package id.parking.core.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PrefApp(context: Context) {

    companion object{
        const val LOGIN = "login"
    }

    private var pref: SharedPreferences = context.getSharedPreferences("parking-system", Context.MODE_PRIVATE)

    fun saveLogin(dataJson: String) {
        pref.edit {
            putString(LOGIN, dataJson)
        }
    }

    fun getLogin(): String {
        return pref.getString(LOGIN, "").toString()
    }

    fun clear() {
        pref.edit {
            remove(LOGIN)
        }
    }

}