package org.d3if4055.barbershop.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import org.d3if4055.barbershop.MainActivity

import org.d3if4055.barbershop.R
import org.d3if4055.barbershop.database.BarberShop
import org.d3if4055.barbershop.databinding.FragmentTambahTransaksiBinding
import org.d3if4055.barbershop.utils.rupiah

@Suppress("SpellCheckingInspection")
class TambahTransaksiFragment : Fragment() {

    private lateinit var binding: FragmentTambahTransaksiBinding
    private var paket = ""
    private var harga = 0.0
    private var kembalian = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_tambah_transaksi, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cekHarga(binding.etTotalHarga)

        binding.btnProses.setOnClickListener {
            inputCheck()
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
                Snackbar.make(requireView(),
                    getString(R.string.null_field, "Nama pelanggan"), Snackbar.LENGTH_SHORT).show()
            }
            binding.etTotalHarga.text.trim().isEmpty() -> {
                Snackbar.make(requireView(),
                    getString(R.string.null_field, "Total harga"), Snackbar.LENGTH_SHORT).show()
            }
            binding.etTotalBayar.text.trim().isEmpty() -> {
                Snackbar.make(requireView(),
                    getString(R.string.null_field, "Total bayar"), Snackbar.LENGTH_SHORT).show()
            }
            else -> doProcess()
        }
    }

    private fun doProcess() {
        val totalBayar = binding.etTotalBayar.text.toString().toDouble()

        if (hitungTransaksi(totalBayar, harga)) {
            val nama = binding.etNamaPelanggan.text.toString()
            val barberShop = BarberShop(nama, paket, harga, totalBayar, kembalian)

            TambahTransaksiDialogFragment(barberShop).show(childFragmentManager, "")
        } else {
            Snackbar.make(requireView(),
                getString(R.string.uang_kurang), Snackbar.LENGTH_SHORT).show()
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
        inflater.inflate(R.menu.reset, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.item_reset -> {
                binding.etNamaPelanggan.setText("")
                binding.rgMenu.clearCheck()
                binding.etTotalHarga.setText("")
                binding.etTotalBayar.setText("")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Transaksi"
    }

}
