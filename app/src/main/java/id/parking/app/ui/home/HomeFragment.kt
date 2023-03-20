package id.parking.app.ui.home

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import id.parking.app.R
import id.parking.app.base.BaseFragment
import id.parking.app.databinding.HomeFragmentBinding
import id.parking.app.model.Lock
import id.parking.app.ui.home.adapter.LockAdapter
import id.parking.app.ui.main.MainFragment
import id.parking.core.firestore.FDBApp

class HomeFragment: BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private var adapter: LockAdapter? = null

    override fun setBinding(view: View): HomeFragmentBinding {
        return HomeFragmentBinding.bind(view)
    }

    override fun onViewInit() {
        initRv()
        initListener()
        getLocks()
    }

    private fun getLocks() {
        FDBApp.getAll<Lock>().observe(viewLifecycleOwner) {
            it.onLoading { show -> showLoading(show) }
            it.onError { msg -> msg?.toast() }
            it.onSuccess { dt -> showData(dt) }
        }
    }

    private fun showData(dt: List<Lock>) {
        dt.forEach { if (it.userId == MainFragment.loginUser.id)  {
            adapter?.addData(it)
        }}

        binding?.layoutEmpty?.apply {
            visibility = if (adapter?.data?.isEmpty() == true) View.VISIBLE
            else View.GONE
        }
    }

    private fun initListener() {
        binding?.buttonAddLock?.setOnClickListener {
            nav?.launchLockAdd(MainFragment.loginUser.id)
        }
    }

    private fun showLoading(show: Boolean) {
        binding?.progressDevice?.apply {
            visibility = if (show) View.VISIBLE
            else View.GONE
        }
    }

    private fun initRv() {
        adapter = LockAdapter(R.layout.home_item) {
            nav?.launchLock(it)
        }
        binding?.rvDevice?.layoutManager = GridLayoutManager(requireContext(), 2)
        binding?.rvDevice?.adapter = adapter
    }

}