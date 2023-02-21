package com.decode.microtic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.decode.microtic.R
import com.decode.microtic.data.AuthInstance
import com.decode.microtic.data.adapters.CategoriaAdapter
import com.decode.microtic.data.adapters.ProdutsAdapter
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentHomeBinding
import com.decode.microtic.ui.activities.CategoriaActivity
import com.decode.microtic.ui.activities.LoginActivity
import com.decode.microtic.ui.viewmodels.HomeFragmentViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeFragmentViewModel
     var adapter: ProdutsAdapter? = null
     var mutableList: MutableList<Devices> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        binding.lytProdutsButton.visibility = View.GONE
        viewModel._devices.observeForever {
            if(!it.isNullOrEmpty()) {
                adapter = ProdutsAdapter(it, requireActivity())
                binding.rcvProduts.adapter = adapter
                adapter!!.notifyDataSetChanged()
                binding.lytProdutsButton.visibility = View.VISIBLE
            }
        }

        binding.txtSeeAllCategory.setOnClickListener {
            startActivity(Intent( requireActivity(), CategoriaActivity::class.java))
        }



        binding.editPesquisa.addTextChangedListener {
            Log.d("searchable", it.toString())
            mutableList.clear()
            for (devices in viewModel._devices.value!!){
                if (devices.marca!!.contains(it.toString(),true) or devices.qrCode!!.contains(it.toString(),true)){
                    mutableList.add(devices)
                }
            }
            adapter = ProdutsAdapter(mutableList, requireActivity())
            binding.rcvProduts.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }

        binding.rcvCategorias.adapter = CategoriaAdapter()


        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mCategoria -> {
                    startActivity(Intent( requireActivity(), CategoriaActivity::class.java))
                    true
                }
                R.id.mlogaout -> {
                    AuthInstance.getInstance()?.signOut()
                    var intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllData()
        var it= viewModel._devices.value
                adapter = it?.let {
                        it1 -> ProdutsAdapter(it1, requireActivity())
                }
                binding.rcvProduts.adapter = adapter
                binding.lytProdutsButton.visibility = View.VISIBLE
            }



        }




