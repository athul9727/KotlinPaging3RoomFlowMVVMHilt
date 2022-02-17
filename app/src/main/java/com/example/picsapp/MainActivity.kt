package com.example.picsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.picsapp.util.replaceFragment
import com.example.picsapp.view.ImagesFragment
import com.example.picsapp.viewmodel.MainActivityViewModel
import com.example.picsapp.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

                viewModel.connectivityLiveData.observe(this, Observer { isAvailable ->
                when (isAvailable) {
                true -> {
                            viewModel.getalldatafromapi()

                }
                false -> {
                    Toast.makeText(this,"No Network", Toast.LENGTH_LONG).show()
                }
            }
        })


        addImagesFragment()
    }

    private fun addImagesFragment() {
        replaceFragment(
            ImagesFragment(),
            R.id.fragment_container
        )
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}