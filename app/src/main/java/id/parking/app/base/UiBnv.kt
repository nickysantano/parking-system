package id.parking.app.base

import com.google.android.material.bottomnavigation.BottomNavigationView

class UiBnv(
    bnv: BottomNavigationView?,
    menuIds: List<Int>,
    listener: (Int) -> Unit
) {

    init {
        bnv?.setOnItemSelectedListener {
            listener(menuIds.indexOf(it.itemId))
            return@setOnItemSelectedListener true
        }
    }

}