package id.parking.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import id.parking.app.model.Lock
import id.parking.app.model.User
import id.parking.app.ui.account.SignInFragment
import id.parking.app.ui.account.SignUpFragment
import id.parking.app.ui.lock.LockAddFragment
import id.parking.app.ui.lock.LockFragment
import id.parking.app.ui.main.MainFragment
import id.parking.app.ui.start.StartFragment

class Nav(private var activity: FragmentActivity) {

    private fun replace(fragment: Fragment, name: String = "") {
        activity.supportFragmentManager.commit {
            replace(R.id.main_container, fragment, name)
            if (name.isNotBlank()) addToBackStack(name)
        }
    }

    private fun add(fragment: Fragment, name: String = "") {
        activity.supportFragmentManager.commit {
            add(R.id.main_container, fragment, name)
            addToBackStack(name)
        }
    }

    private fun clear() {
        activity.supportFragmentManager.apply {
            while (backStackEntryCount != 0) {
                popBackStackImmediate()
            }
        }
    }

    fun startApp() {
        replace(StartFragment())
    }

    fun launchSignIn() {
        replace(SignInFragment())
    }

    fun launchSignUp() {
        replace(SignUpFragment())
    }

    fun launchMain(user: User) {
        clear()
        MainFragment.loginUser = user
        replace(MainFragment())
    }

    fun launchLock(lock: Lock) {
        replace(LockFragment(lock),"LockFragment")
    }

    fun launchLockAdd(userId: String) {
        replace(LockAddFragment(userId), "LockAddFragment")
    }

}