package org.d3if4055.barbershop.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import org.d3if4055.barbershop.MainActivity

import org.d3if4055.barbershop.R
import org.d3if4055.barbershop.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.fabAddTransaksi.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tambahTransaksiFragment)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Barber Shop"
    }
}
