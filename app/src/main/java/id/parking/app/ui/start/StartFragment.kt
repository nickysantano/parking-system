package id.parking.app.ui.start

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.StartFragmentBinding
import id.parking.app.model.User
import id.parking.core.pref.PrefApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartFragment: BaseFragment<StartFragmentBinding>(R.layout.start_fragment) {

    override fun setBinding(view: View): StartFragmentBinding {
        return StartFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        lifecycleScope.launch(Dispatchers.IO) {
            Thread.sleep(2000)
            withContext(Dispatchers.Main) {
                checkLogin()
            }
        }
    }

    private fun checkLogin() {
        PrefApp(requireContext()).getLogin().apply {
            if (isNotBlank()) nav?.launchMain(
                Gson().fromJson(this, User::class.java)
            )
            else nav?.launchSignIn()
        }
    }

}