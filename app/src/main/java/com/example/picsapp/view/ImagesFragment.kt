package com.example.picsapp.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picsapp.MainActivity
import com.example.picsapp.R
import com.example.picsapp.databinding.FragmentImagesBinding
import com.example.picsapp.repository.model.ImageListResponseItem
import com.example.picsapp.util.SwipeToDeleteCallback
import com.example.picsapp.util.replaceFragment
import com.example.picsapp.viewmodel.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.starapps.kotlinmvvm.view.ImageClickListener
import com.starapps.kotlinmvvm.view.RecAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ImagesFragment : Fragment()  , ImageClickListener {

    private val viewModel: ViewModel by viewModels()
    private lateinit var recAdapter: RecAdapter
    private lateinit var mDataBinding: FragmentImagesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mDataBinding  = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_images,
            container,
            false)

        val mRootView = mDataBinding.root
        mDataBinding.lifecycleOwner = this
        return mRootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setView()
        removeBackButton()
        enableSwipeToDeleteAndUndo();
        mDataBinding.viewModel = viewModel

        lifecycleScope.launch {
            viewModel.imagePostFlow.collectLatest {
                recAdapter.submitData(it)
            }
        }


        viewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
        })


        mDataBinding.togglebutton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mDataBinding.recycleview.layoutManager = LinearLayoutManager(activity)
                mDataBinding.recycleview.adapter = recAdapter
            } else {
                // The toggle is disabled
                mDataBinding.recycleview.layoutManager = GridLayoutManager(activity,2)
                mDataBinding.recycleview.adapter = recAdapter
            }
        }

//        recAdapter.addLoadStateListener { loadState ->
//
//            /**
//            This code is taken from https://medium.com/@yash786agg/jetpack-paging-3-0-android-bae37a56b92d
//             **/
//
//            if (loadState.refresh is LoadState.Loading){
//                progressBar.visibility = View.VISIBLE
//            }
//            else{
//                progressBar.visibility = View.GONE
//
//                // getting the error
//                val error = when {
//                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
//                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
//                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
//                    else -> null
//                }
//                errorState?.let {
//                    Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        }

    }

    private fun removeBackButton() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? AppCompatActivity)?.supportActionBar?.setHomeButtonEnabled(false)
    }

    private fun setView() {
        recAdapter = RecAdapter(context, this)
        mDataBinding.recycleview.layoutManager = LinearLayoutManager(activity)
        mDataBinding.recycleview.setHasFixedSize(true)
        mDataBinding.recycleview.setItemViewCacheSize(12)
        recAdapter.withLoadStateFooter(
            footer = RecLoadStateAdapter { recAdapter.retry() }
        )
        mDataBinding.recycleview.adapter = recAdapter
        mDataBinding.togglebutton.setChecked(true);

    }

    override fun onItemClick(imagedata : ImageListResponseItem) {
        (activity as MainActivity).replaceFragment(
            ImageDetailsFragment.newInstance(imagedata),
            R.id.fragment_container, "imagedetails")
    }


    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val item = recAdapter.getData(position)
                item?.let { viewModel.deleteitem(it) }
                //recAdapter.removeItem(position)
            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(mDataBinding.recycleview)
    }

}