package org.d3if4055.barbershop.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.dialog_fragment_tambah_transaksi.*

import org.d3if4055.barbershop.R
import org.d3if4055.barbershop.database.BarberShop
import org.d3if4055.barbershop.database.BarberShopDatabase
import org.d3if4055.barbershop.utils.rupiah
import org.d3if4055.barbershop.viewmodel.BarberShopViewModel
import org.d3if4055.barbershop.viewmodel.BarberShopViewModelFactory

@Suppress("SpellCheckingInspection")
class UpdateTransaksiDialogFragment(
    private val dataBarberShop: BarberShop
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_update_transaksi, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_MinWidth)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditText()

        // call view model
        val application = requireNotNull(this.activity).application
        val dataSource = BarberShopDatabase.getInstance(application).barberShopDAO
        val viewModelFactory = BarberShopViewModelFactory(dataSource, application)
        val barberShopViewModel = ViewModelProvider(this, viewModelFactory).get(BarberShopViewModel::class.java)

        btn_submit_transaksi.setOnClickListener {
            barberShopViewModel.onClickUpdate(dataBarberShop)
            this.dismiss()
            this.findNavController().popBackStack()
        }
    }

    private fun setEditText() {
        et_nama_pelanggan.setText(dataBarberShop.nama)
        et_paket.setText(dataBarberShop.paket)
        et_total_harga.setText(rupiah(dataBarberShop.harga))
        et_total_bayar.setText(rupiah(dataBarberShop.bayar))
        et_kembalian.setText(rupiah(dataBarberShop.kembalian))
    }

}
