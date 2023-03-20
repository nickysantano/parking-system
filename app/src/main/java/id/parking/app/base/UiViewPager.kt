package id.parking.app.base

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UiViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val listFragment = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment){
        listFragment.add(fragment)
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }

}