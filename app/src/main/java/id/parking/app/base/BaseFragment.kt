package id.parking.app.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import id.parking.app.Nav

abstract class BaseFragment<T>(@LayoutRes layoutRes: Int): Fragment(layoutRes) {

    var nav: Nav? = null
    var binding: T? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = Nav(requireActivity())
        binding = setBinding(view)
        onViewInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    abstract fun setBinding(view: View): T

    abstract fun onViewInit()

    fun String.toast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }

}