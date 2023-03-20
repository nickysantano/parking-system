package id.parking.app.ui.account

import android.view.View
import com.google.gson.Gson
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.SigninFragmentBinding
import id.parking.app.model.User
import id.parking.app.ui.main.MainFragment
import id.parking.core.firestore.FDBApp
import id.parking.core.pref.PrefApp

class SignInFragment: BaseFragment<SigninFragmentBinding>(R.layout.signin_fragment) {

    private var userId: String = ""
    private var password: String = ""

    override fun setBinding(view: View): SigninFragmentBinding {
        return SigninFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initListener()
    }

    private fun initListener() {
        binding?.buttonSignIn?.setOnClickListener {
            if (inputComplete()) requestSignIn()
            else "Please Complete Input.".toast()
        }

        binding?.buttonSignUp?.setOnClickListener {
            nav?.launchSignUp()
        }
    }

    private fun requestSignIn() {
        FDBApp.getAll<User>().observe(viewLifecycleOwner) {
            it.onLoading { show -> showLoading(show) }
            it.onError { msg -> msg?.toast() }
            it.onSuccess { dt -> showData(dt) }
        }
    }

    private fun showData(dt: List<User>) {
        dt.forEach {
            if (it.id == userId && it.password == password) {
                PrefApp(requireContext()).saveLogin(Gson().toJson(it))
                "Login Successfully".toast()
                nav?.launchMain(it)
                return
            }
        }
        "Login Failed".toast()
    }

    private fun showLoading(show: Boolean) {
        binding?.progressSignIn?.apply {
            visibility = if (show) View.VISIBLE
            else View.GONE
        }

        binding?.buttonSignIn?.apply {
            visibility = if (show) View.INVISIBLE
            else View.VISIBLE
        }
    }

    private fun inputComplete(): Boolean {
        userId = binding?.inputUserId?.editText?.text.toString()
        password = binding?.inputPassword?.editText?.text.toString()
        if (userId.isBlank()) return false
        if (password.isBlank()) return false
        return true
    }

}