package org.fossasia.badgemagic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.fossasia.badgemagic.data.Message

class TransferQueueViewModel : ViewModel() {
    private val _items = MutableLiveData<ArrayList<Message>>()
    val items: LiveData<ArrayList<Message>>
        get() = _items

    init {
        _items.value = ArrayList()
    }

    fun add(item: Message) {
        _items.value?.add(item)
        if (_items.value!!.size > 8) {
            _items.value?.removeAt(0)
        }
    }

    fun remove(item: Message) {
        _items.value?.remove(item)
    }
}
