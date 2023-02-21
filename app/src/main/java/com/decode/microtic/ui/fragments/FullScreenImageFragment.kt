package com.decode.microtic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.decode.microtic.R
import com.decode.microtic.data.models.Devices
import com.decode.microtic.databinding.FragmentFullScreenImageBinding
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FullScreenImageFragment : Fragment() {

    lateinit var binding: FragmentFullScreenImageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_full_screen_image, container, false)
        binding = FragmentFullScreenImageBinding.bind(view)

        var bundle = requireActivity().intent.getParcelableExtra<Devices>("device")
        if(bundle!=null){
            GlobalScope.launch (Dispatchers.Main) {
                val imageList = ArrayList<SlideModel>()
                bundle.photoUrls.forEach {
                    imageList.add(SlideModel(it, "${bundle.nomeProduto} - ${bundle.qrCode}"))
                }
                binding.imageSlider.setImageList(imageList)
                binding.imageSlider.stopSliding()
            }
        }



        return binding.root
    }


}