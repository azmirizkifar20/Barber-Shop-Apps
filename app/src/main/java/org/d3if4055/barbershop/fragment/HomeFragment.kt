package org.d3if4055.barbershop.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.d3if4055.barbershop.MainActivity

import org.d3if4055.barbershop.R
import org.d3if4055.barbershop.database.BarberShop
import org.d3if4055.barbershop.database.BarberShopDatabase
import org.d3if4055.barbershop.databinding.FragmentHomeBinding
import org.d3if4055.barbershop.recyclerview.BarberShopAdapter
import org.d3if4055.barbershop.recyclerview.RecyclerViewClickListener
import org.d3if4055.barbershop.viewmodel.BarberShopViewModel
import org.d3if4055.barbershop.viewmodel.BarberShopViewModelFactory

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class HomeFragment : Fragment(),
    RecyclerViewClickListener {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewModel().barberShop.observe(viewLifecycleOwner, Observer {
            val adapter = BarberShopAdapter(it)
            val recyclerView = binding.rvBarbershop
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

            // set listener
            adapter.listener = this
        })

        binding.fabAddTransaksi.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tambahTransaksiFragment)
        }
    }

    private fun getViewModel(): BarberShopViewModel {
        // get viewmodel
        val application = requireNotNull(this.activity).application
        val dataSource = BarberShopDatabase.getInstance(application).barberShopDAO
        val viewModelFactory = BarberShopViewModelFactory(dataSource, application)
        return ViewModelProvider(this, viewModelFactory).get(BarberShopViewModel::class.java)
    }

    override fun onRecyclerViewItemClicked(view: View, barberShop: BarberShop) {
        val bundle = Bundle()
        bundle.putLong("id", barberShop.id)
        bundle.putString("nama", barberShop.nama)
        bundle.putString("paket", barberShop.paket)
        bundle.putDouble("harga", barberShop.harga)
        bundle.putDouble("bayar", barberShop.bayar)
        bundle.putDouble("kembalian", barberShop.kembalian)
        bundle.putLong("tanggal", barberShop.tanggal)

        when(view.id) {
            R.id.list_data_cukur -> {
                view.findNavController().navigate(R.id.action_homeFragment_to_editTransaksiFragment, bundle)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.item_delete -> {
                AlertDialog.Builder(requireContext()).also {
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        getViewModel().onClickClear()
                        Snackbar.make(requireView(), getString(R.string.success_delete), Snackbar.LENGTH_SHORT).show()
                    }
                }.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Barber Shop"
    }
}
