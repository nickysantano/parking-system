package id.parking.app

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class App: LocalizationApplication() {

    override fun getDefaultLanguage(context: Context): Locale {
        return Locale.getDefault()
    }

}