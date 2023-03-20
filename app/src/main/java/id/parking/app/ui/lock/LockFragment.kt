package id.parking.app.ui.lock

import android.graphics.Color
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.LockFragmentBinding
import id.parking.app.model.Lock
import id.parking.core.rdb.LockData
import id.parking.core.rdb.RDBApp

class LockFragment(var lock: Lock): BaseFragment<LockFragmentBinding>(R.layout.lock_fragment) {

    private var lockData: LockData? = null

    private fun lockId(): String {
        return "${lock.userId}-${lock.id}".replace(".","")
            .replace("#", "")
            .replace("$", "")
            .replace("[","")
            .replace("]", "")
    }

    override fun setBinding(view: View): LockFragmentBinding {
        return LockFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initListener()
        setUi()
        checkLock()
    }

    private fun checkLock() {
        RDBApp.getId(lockId()).observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onLoading { show -> showLoading(show) }
            it.onSuccess { dt -> if (dt) getLockData() else setNewLock() }
        }
    }

    private fun setNewLock() {
        RDBApp.setLock(lockId(), LockData()).observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onLoading { show -> showLoading(show) }
            it.onSuccess { dt ->
                listenLockData()
                "New lock data has been set".toast()
            }
        }
    }

    private fun getLockData() {
        RDBApp.getLock(lockId()).observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onSuccess { dt -> setLockData(dt) }
            it.onLoading { show -> showLoading(show) }
        }
        listenLockData()
    }

    private fun listenLockData() {
        RDBApp.listenLock(lockId()).observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onSuccess { dt -> setLockData(dt) }
        }
    }

    private fun setLockData(dt: LockData) {
        lockData = dt
        binding?.textStatus?.apply {
            text = dt.status
            if (dt.status.lowercase() == "detected") {
                setTextColor(Color.parseColor("#4CAF50"))
            } else if (dt.status.lowercase() == "undetected") {
                setTextColor(Color.parseColor("#F44336"))
            } else {
                setTextColor(Color.parseColor("#8A8A8A"))
            }
            "Lock Status: ${dt.status}".toast()
        }
        binding?.buttonLockAction?.apply {
            text = when (dt.locked) {
                1 -> {
                    setBackgroundColor(Color.parseColor("#8A8A8A"))
                    "Unlock"
                }
                0 -> {
                    setBackgroundColor(Color.parseColor("#4CAF50"))
                    "Lock"
                }
                else -> "-"
            }
            if (dt.status.lowercase() == "undetected") {
                setBackgroundColor(Color.parseColor("#F44336"))
                text = "Problem"
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding?.progressLock?.apply {
            visibility = if (show) View.VISIBLE
            else View.GONE
        }

        binding?.buttonLockAction?.apply {
            visibility = if (show) View.INVISIBLE
            else View.VISIBLE
        }
    }

    private fun setUi() {
        binding?.textLockId?.text = lock.id
    }

    private fun initListener() {
        binding?.tbLock?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.buttonLockAction?.setOnClickListener {
            lockData?.let { dt -> lockAction(dt) }
        }
    }

    private fun lockAction(dt: LockData) {
        if (dt.status.lowercase() == "undetected") dialogUndetected()
        else {
            if (dt.locked == 0) dialogLocked()
            else if (dt.locked == 1) dialogUnlocked()
        }
    }

    private fun dialogLocked() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Parking Spot: ${lock.id}")
            .setMessage("Are you sure you want lock it? Make sure that you close the lock correctly. Once you confirm, the locking mechanism will turned on and lock your motorcycle.")
            .setNegativeButton("Cancel") { a, b ->
                a.dismiss()
            }
            .setPositiveButton("Confirm") { a, b ->
                a.dismiss()
                requestLock()
                requestDetected()
            }
            .show()
    }

    private fun dialogUnlocked() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Parking Spot: ${lock.id}")
            .setMessage("Do you want to unlock ? Once you confirm this alert, the lock will open and you can use your motorcycle again.")
            .setNegativeButton("Cancel") { a, b ->
                a.dismiss()
            }
            .setPositiveButton("Confirm") { a, b ->
                a.dismiss()
                requestUnlock()
                requestStrip()
            }
            .show()
    }

    private fun dialogUndetected() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Parking Spot: ${lock.id}")
            .setMessage("Looks like our sensor didnt detect your motorcycle and you didnt confirming if you are unlocking the lock mechanism.\nPlease check your motorcycle, if it wasnt there, please do contact the authorities")
            .setNegativeButton("Close") { a, b ->
                a.dismiss()
            }
            .setPositiveButton("Its There") { a, b ->
                a.dismiss()
                requestDetected()
            }
            .show()
    }

    private fun requestDetected() {
        RDBApp.setStatus(lockId(), "detected").observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onLoading { show -> showLoading(show) }
            it.onSuccess { dt -> "Lock Detected Successfully".toast() }
        }
    }

    private fun requestLock() {
        RDBApp.setLocked(lockId(),1).observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onLoading { show -> showLoading(show) }
            it.onSuccess { dt -> "Locked Successfully".toast() }
        }
    }

    private fun requestUnlock() {
        RDBApp.setLocked(lockId(),0).observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onLoading { show -> showLoading(show) }
            it.onSuccess { dt -> "Unlocked Successfully".toast() }
        }
    }

    private fun requestStrip() {
        RDBApp.setStatus(lockId(), "-").observe(viewLifecycleOwner) {
            it.onError { msg -> msg?.toast() }
            it.onLoading { show -> showLoading(show) }
        }
    }

}