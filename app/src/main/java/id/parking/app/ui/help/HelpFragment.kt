package id.parking.app.ui.help

import android.view.View
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.HelpFragmentBinding

class HelpFragment: BaseFragment<HelpFragmentBinding>(R.layout.help_fragment) {

    override fun setBinding(view: View): HelpFragmentBinding {
        return HelpFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initUi()
        initListener()
    }

    private fun initUi() {

    }

    private fun initListener() {

    }

}