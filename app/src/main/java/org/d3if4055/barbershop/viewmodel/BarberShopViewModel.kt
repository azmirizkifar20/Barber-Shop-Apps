package org.d3if4055.barbershop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import org.d3if4055.barbershop.database.BarberShop
import org.d3if4055.barbershop.database.BarberShopDAO

class BarberShopViewModel(
    val database: BarberShopDAO,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope =   CoroutineScope(Dispatchers.Main + viewModelJob)
    val barberShop = database.getBarberShop()

    fun onClickInsert(barberShop: BarberShop) {
        uiScope.launch {
            insert(barberShop)
        }
    }

    private suspend fun insert(barberShop: BarberShop) {
        withContext(Dispatchers.IO) {
            database.insert(barberShop)
        }
    }

    fun onClickUpdate(barberShop: BarberShop) {
        uiScope.launch {
            update(barberShop)
        }
    }

    private suspend fun update(barberShop: BarberShop) {
        withContext(Dispatchers.IO) {
            database.update(barberShop)
        }
    }

    fun onClickClear() {
        uiScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun onClickDelete(id: Long) {
        uiScope.launch {
            delete(id)
        }
    }

    private suspend fun delete(id: Long) {
        withContext(Dispatchers.IO) {
            database.delete(id)
        }
    }

}