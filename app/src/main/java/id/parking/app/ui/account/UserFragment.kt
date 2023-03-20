package id.parking.app.ui.account

import android.view.View
import com.google.gson.Gson
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.UserFragmentBinding
import id.parking.app.model.User
import id.parking.core.pref.PrefApp

class UserFragment: BaseFragment<UserFragmentBinding>(R.layout.user_fragment) {

    override fun setBinding(view: View): UserFragmentBinding {
        return UserFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initListener()
        setUi()
    }

    private fun setUi() {
        PrefApp(requireContext()).getLogin().let {
            if (it.isNotBlank()) {
                val user = Gson().fromJson(it, User::class.java)
                binding?.textUserName?.text = user.name
                binding?.textUserEmail?.text = user.id
            }
        }
    }

    private fun initListener() {
        binding?.buttonLogout?.setOnClickListener {
            PrefApp(requireContext()).clear()
            nav?.launchSignIn()
        }
    }

}