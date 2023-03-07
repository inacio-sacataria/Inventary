package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.decode.microtic.R
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentFullScreenImageBinding
import com.decode.microtic.showInfo
import com.decode.microtic.ui.viewmodels.RegistDevicesViewModel
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FullScreenImageFragment : Fragment() {

    lateinit var binding: FragmentFullScreenImageBinding
    lateinit var viewModel: RegistDevicesViewModel
    val imageList = ArrayList<SlideModel>()
    var fotos = mutableListOf<String>()
    lateinit var device: Devices
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_full_screen_image, container, false)
        binding = FragmentFullScreenImageBinding.bind(view)
        viewModel = ViewModelProvider(this).get(RegistDevicesViewModel::class.java)
        device = requireActivity().intent.getParcelableExtra<Devices>("device")!!
        if (device != null) {
            GlobalScope.launch(Dispatchers.Main) {
                device.photoUrls.forEach {
                    imageList.add(SlideModel(it, "${device.nomeProduto} - ${device.qrCode}"))
                }
                binding.imageSlider.setImageList(imageList)
                binding.imageSlider.stopSliding()

                binding.imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun onItemSelected(position: Int) {
                        requireActivity().showInfo(position.toString())
                    }

                })

                var pos: Int = 3000
                binding.imageSlider.setItemChangeListener(object : ItemChangeListener {
                    override fun onItemChanged(position: Int) {
                        pos = position
                    }

                })

                binding.btnImgDelete.setOnClickListener {
                    if (pos == 3000) {
                        pos = 0
                        requireActivity().showInfo("Deleted ${pos.toString()}")
                        imageList.removeAt(pos)
                    } else {
                        requireActivity().showInfo("Deleted ${pos.toString()}")
                        imageList.removeAt(pos)
                    }

                    dialogShow(device)
                }
            }
        }

        return binding.root
    }

    fun dialogShow(device: Devices) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(("Deseja apagar esta foto?"))
            .setMessage(("Apagando esta foto , significa que não vai poder recupera-la."))
            .setPositiveButton("Apagar") { dialog, which ->
                imageList.forEach {
                    fotos.add(it.imageUrl!!)
                }
                binding.imageSlider.setImageList(imageList)
                binding.imageSlider.stopSliding()
                device.photoUrls = fotos
                viewModel.updatePhotoUrl(device)
            }
            .setNegativeButton("Cancelar") { dialog, which ->


            }.setCancelable(false)
            .show()

    }
}