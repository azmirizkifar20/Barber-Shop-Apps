package org.d3if4055.barbershop.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import org.d3if4055.barbershop.MainActivity

import org.d3if4055.barbershop.R
import org.d3if4055.barbershop.database.BarberShop
import org.d3if4055.barbershop.database.BarberShopDatabase
import org.d3if4055.barbershop.databinding.FragmentEditTransaksiBinding
import org.d3if4055.barbershop.utils.rupiah
import org.d3if4055.barbershop.viewmodel.BarberShopViewModel
import org.d3if4055.barbershop.viewmodel.BarberShopViewModelFactory

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class EditTransaksiFragment : Fragment() {

    private lateinit var binding: FragmentEditTransaksiBinding
    private var paket = ""
    private var harga = 0.0
    private var kembalian = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_transaksi, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set input text
        binding.etNamaPelanggan.setText(getBundle()!!.nama)
        binding.etTotalHarga.setText(rupiah(getBundle()!!.harga))
        binding.etTotalBayar.setText("${getBundle()!!.bayar.toInt()}")

        // radio button check
        when (getBundle()?.paket) {
            "Haircut" -> binding.rbMenu1.isChecked = true
            "Haircut & Wash" -> binding.rbMenu2.isChecked = true
            "Full Service" -> binding.rbMenu3.isChecked = true
        }

        // set default paket & harga
        paket = getBundle()!!.paket
        harga = getBundle()!!.harga

        // cek harga lewat radio button
        cekHarga(binding.etTotalHarga)

        // onclick transaction
        binding.btnProses.setOnClickListener {
            inputCheck()
        }

    }

    private fun getBundle(): BarberShop? {
        if (arguments != null) {
            val id = arguments!!.getLong("id")
            val nama = arguments!!.getString("nama")
            val paket = arguments!!.getString("paket")
            val harga = arguments!!.getDouble("harga")
            val bayar = arguments!!.getDouble("bayar")
            val kembalian = arguments!!.getDouble("kembalian")
            val tanggal = arguments!!.getLong("tanggal")

            return BarberShop(id, nama.toString(), paket.toString(), harga, bayar, kembalian, tanggal)
        } else {
            return null
        }
    }

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    private fun cekHarga(etHarga: EditText) {
        binding.rgMenu.setOnCheckedChangeListener{ group, checkedId ->
            when {
                binding.rbMenu1.isChecked -> {
                    harga = 30000.0
                    paket = "Haircut"
                    etHarga.setText(rupiah(harga))
                }
                binding.rbMenu2.isChecked -> {
                    harga = 35000.0
                    paket = "Haircut & Wash"
                    etHarga.setText(rupiah(harga))
                }
                binding.rbMenu3.isChecked -> {
                    harga = 45000.0
                    paket = "Full Service"
                    etHarga.setText(rupiah(harga))
                }
            }
        }
    }

    private fun inputCheck() {
        when {
            binding.etNamaPelanggan.text.trim().isEmpty() -> {
                binding.inputLayoutNama.error = getString(R.string.null_field, "Nama pelanggan")
            }
            binding.etTotalHarga.text.trim().isEmpty() -> {
                binding.inputLayoutTotalHarga.error = getString(R.string.null_field, "Total harga")
            }
            binding.etTotalBayar.text.trim().isEmpty() -> {
                binding.inputLayoutTotalBayar.error = getString(R.string.null_field, "Total bayar")
            }
            else -> doProcess()
        }
    }

    private fun doProcess() {
        val totalBayar = binding.etTotalBayar.text.toString().toDouble()

        if (hitungTransaksi(totalBayar, harga)) {
            val nama = binding.etNamaPelanggan.text.toString()

            val barberShop = BarberShop(getBundle()!!.id, nama, paket, harga, totalBayar, kembalian, System.currentTimeMillis())
            UpdateTransaksiDialogFragment(barberShop).show(childFragmentManager, "")
        } else {
            binding.inputLayoutTotalBayar.error = getString(R.string.uang_kurang)
        }
    }

    private fun hitungTransaksi(jumlahUang: Double, harga: Double): Boolean {
        return when {
            jumlahUang >= harga -> {
                kembalian = jumlahUang - harga
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete, menu)
        inflater.inflate(R.menu.reset, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // call view model
        val application = requireNotNull(this.activity).application
        val dataSource = BarberShopDatabase.getInstance(application).barberShopDAO
        val viewModelFactory = BarberShopViewModelFactory(dataSource, application)
        val barberShopViewModel = ViewModelProvider(this, viewModelFactory).get(BarberShopViewModel::class.java)

        return when(item.itemId) {
            R.id.item_delete -> {

                AlertDialog.Builder(requireContext()).also {
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        barberShopViewModel.onClickDelete(getBundle()!!.id)
                        this.findNavController().popBackStack()
                        Snackbar.make(requireView(), getString(R.string.success_delete), Snackbar.LENGTH_SHORT).show()
                    }
                }.create().show()

                true
            }
            R.id.item_reset -> {
                reset()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun reset() {
        binding.etNamaPelanggan.setText("")
        binding.inputLayoutNama.error = null
        binding.rgMenu.clearCheck()
        binding.etTotalHarga.setText("")
        binding.inputLayoutTotalHarga.error = null
        binding.etTotalBayar.setText("")
        binding.inputLayoutTotalBayar.error = null
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Transaksi"
    }
}
