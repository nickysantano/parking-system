package id.parking.app.ui.account

import android.view.View
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.SignupFragmentBinding
import id.parking.app.model.User
import id.parking.core.firestore.FDBApp
import id.parking.core.firestore.Inserter

class SignUpFragment: BaseFragment<SignupFragmentBinding>(R.layout.signup_fragment) {

    private var name: String = ""
    private var userId: String = ""
    private var password: String = ""

    override fun setBinding(view: View): SignupFragmentBinding {
        return SignupFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initListener()
    }

    private fun initListener() {
        binding?.buttonSignUp?.setOnClickListener {
            if (inputComplete()) requestSignUp()
            else "Please Complete Input.".toast()
        }

        binding?.buttonSignIn?.setOnClickListener {
            nav?.launchSignIn()
        }
    }

    private fun inputComplete(): Boolean {
        name = binding?.inputName?.editText?.text.toString()
        userId = binding?.inputUserId?.editText?.text.toString()
        password = binding?.inputPassword?.editText?.text.toString()
        if (name.isBlank()) return false
        if (userId.isBlank()) return false
        if (password.isBlank()) return false
        return true
    }

    private fun requestSignUp() {
        FDBApp.get<User>(userId).observe(viewLifecycleOwner) {
            it.onSuccess { dt -> "Email has been used".toast() }
            it.onError { msg -> if (msg == "Data null") insertUser() }
        }
    }

    private fun insertUser() {
        val user = User(userId, name, password)
        Inserter<User>(user.id).insert(user).observe(viewLifecycleOwner) {
            it.onLoading { show -> showLoading(show) }
            it.onError { msg -> msg?.toast() }
            it.onSuccess { dt -> showData(dt) }
        }
    }

    private fun showData(dt: String) {
        "SignUp Successfully".toast()
        nav?.launchSignIn()
    }

    private fun showLoading(show: Boolean) {
        binding?.progressSignUp?.apply {
            visibility = if (show) View.VISIBLE
            else View.GONE
        }

        binding?.buttonSignUp?.apply {
            visibility = if (show) View.INVISIBLE
            else View.VISIBLE
        }
    }

}