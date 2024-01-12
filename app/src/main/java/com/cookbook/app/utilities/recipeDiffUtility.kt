package com.cookbook.app.utilities

import androidx.recyclerview.widget.DiffUtil
import com.cookbook.app.models.Result

class recipeDiffUtility<T>(
    private val oldResultList: List<T>,
    private val newResultList: List<T>
) : DiffUtil.Callback() {
    // check old list size
    override fun getOldListSize(): Int {
        return oldResultList.size
    }

    // check new list size
    override fun getNewListSize(): Int {
        return newResultList.size
    }

    // check if identical items
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResultList[oldItemPosition] === newResultList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResultList[oldItemPosition] == newResultList[newItemPosition]
    }


}