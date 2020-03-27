package org.d3if4055.barbershop.recyclerview

import android.view.View
import org.d3if4055.barbershop.database.BarberShop

interface RecyclerViewClickListener {

 fun onRecyclerViewItemClicked(view: View, barberShop: BarberShop)

}