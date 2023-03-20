package id.parking.app.ui.main

import android.view.View
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.base.UiBnv
import id.parking.app.base.UiViewPager
import id.parking.app.databinding.MainFragmentBinding
import id.parking.app.model.User
import id.parking.app.ui.account.UserFragment
import id.parking.app.ui.home.HomeFragment
import id.parking.app.ui.help.HelpFragment

class MainFragment: BaseFragment<MainFragmentBinding>(R.layout.main_fragment) {

    companion object {
        var loginUser: User = User()
    }

    override fun setBinding(view: View): MainFragmentBinding {
        return MainFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initVp()
        initBnv()
    }

    private fun initBnv() {
        val menus = listOf(R.id.menu_home, R.id.menu_history, R.id.menu_user)
        UiBnv(binding?.bnvMain, menus) {
            binding?.vpMain?.currentItem = it
        }
    }

    private fun initVp() {
        UiViewPager(this).apply {
            addFragment(HomeFragment())
            addFragment(HelpFragment())
            addFragment(UserFragment())
            binding?.vpMain?.adapter = this
            binding?.vpMain?.isUserInputEnabled = false
        }
    }

}