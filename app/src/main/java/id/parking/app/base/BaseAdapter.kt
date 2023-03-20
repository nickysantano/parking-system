package id.parking.app.base

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class BaseAdapter<T>(layoutResId: Int) :
    BaseQuickAdapter<T, BaseViewHolder>(layoutResId, arrayListOf()) { }