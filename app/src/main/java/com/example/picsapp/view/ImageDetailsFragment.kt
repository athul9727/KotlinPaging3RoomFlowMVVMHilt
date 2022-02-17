package com.example.picsapp.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.picsapp.R
import com.example.picsapp.databinding.FragmentImageDetailsBinding
import com.example.picsapp.repository.model.ImageListResponseItem
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ImageDetailsFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(data: ImageListResponseItem) =
            ImageDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("data_row", data)
                }
            }
    }

    private var imagedata: ImageListResponseItem? = null
    private lateinit var mDataBinding: FragmentImageDetailsBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        imagedata =  arguments?.getParcelable("data_row")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDataBinding  = DataBindingUtil.inflate(inflater,
            R.layout.fragment_image_details, container, false)
        val mRootView = mDataBinding.root
        mDataBinding.lifecycleOwner = this
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableBackButton()
        mDataBinding.imagedata = imagedata
    }

    private fun enableBackButton() {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.setHomeButtonEnabled(true)
    }
}