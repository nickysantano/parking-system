package id.parking.app.ui.lock

import android.view.View
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.LockAddFragmentBinding
import id.parking.app.model.Lock
import id.parking.core.firestore.Inserter

class LockAddFragment(var userId: String): BaseFragment<LockAddFragmentBinding>(R.layout.lock_add_fragment) {

    private var lockId: String = ""
    private var password: String = ""

    override fun setBinding(view: View): LockAddFragmentBinding {
        return LockAddFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initListener()
    }

    private fun initListener() {
        binding?.buttonSave?.setOnClickListener {
            if (inputComplete()) addLock()
            else "Please Complete Input.".toast()
        }
    }

    private fun addLock() {
        Inserter<Lock>(lockId).insert(Lock(lockId, userId, password)).observe(viewLifecycleOwner) {
            it.onLoading { show -> showLoading(show) }
            it.onError { msg -> msg?.toast() }
            it.onSuccess { dt -> showData(dt) }
        }
    }

    private fun showData(dt: String) {
        "Lock Registered Successfully".toast()
        onBackPressed()
    }

    private fun showLoading(show: Boolean) {
        binding?.progressAdd?.apply {
            visibility = if (show) View.VISIBLE
            else View.GONE
        }

        binding?.buttonSave?.apply {
            visibility = if (show) View.INVISIBLE
            else View.VISIBLE
        }
    }

    private fun inputComplete(): Boolean {
        lockId = binding?.inputLockId?.editText?.text.toString()
        password = binding?.inputLockPassword?.editText?.text.toString()
        if (lockId.isBlank()) return false
        if (password.isBlank()) return false
        return true
    }

}