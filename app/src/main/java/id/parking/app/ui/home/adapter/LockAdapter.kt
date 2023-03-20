package id.parking.app.ui.home.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import id.parking.app.base.BaseAdapter
import id.parking.app.databinding.HomeItemBinding
import id.parking.app.model.Lock

class LockAdapter(layoutResId: Int, var listener: (Lock) -> Unit) : BaseAdapter<Lock>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: Lock) {
        val x = HomeItemBinding.bind(holder.itemView)
        x.textLockId.text = item.id
        x.root.setOnClickListener {
            listener(item)
        }
    }
}