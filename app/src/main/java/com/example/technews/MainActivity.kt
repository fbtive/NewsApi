package com.example.technews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.technews.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = findNavController(R.id.mainNavHostFragment)
        appBarConfiguration = AppBarConfiguration
            .Builder(R.id.headline_fragment_dst, R.id.sources_fragment_dst, R.id.bookmark_fragment_dst)
            .build()

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ nc: NavController, nd: NavDestination, args: Bundle? ->
            when(nd.id) {
                R.id.headline_fragment_dst -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                R.id.sources_fragment_dst -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                R.id.bookmark_fragment_dst -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                R.id.articleFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = findNavController(R.id.mainNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}