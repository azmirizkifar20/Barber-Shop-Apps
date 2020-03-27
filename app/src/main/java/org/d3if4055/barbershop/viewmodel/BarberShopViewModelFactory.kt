package org.d3if4055.barbershop.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if4055.barbershop.database.BarberShopDAO
import javax.sql.CommonDataSource

@Suppress("UNCHECKED_CAST")
class BarberShopViewModelFactory(
    private val dataSource: BarberShopDAO,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BarberShopViewModel::class.java)) {
            return BarberShopViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}