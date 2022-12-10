package com.example.technews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.technews.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
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

        WindowCompat.setDecorFitsSystemWindows(window, false)
//        appBarConfiguration = AppBarConfiguration
//            .Builder(R.id.headline_fragment_dst, R.id.sources_fragment_dst, R.id.bookmark_fragment_dst)
//            .build()

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

    fun recalculateToolbarAndRootView(view: View, appBarLayout: AppBarLayout) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { root, windowInset ->
            val inset = windowInset.getInsets(WindowInsetsCompat.Type.systemBars())
            root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = inset.left
                rightMargin = inset.right
                bottomMargin = inset.bottom
                topMargin = 0
            }

            WindowInsetsCompat.CONSUMED
        }

        var statusBarHeight = 0
        var resourceID = resources.getIdentifier("status_bar_height", "dimen", "android")
        if(resourceID>0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceID)
        }

        val typedValue = TypedValue()
        var actionBarHeight = 0
        if(theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight =TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        }
        actionBarHeight += statusBarHeight

        val appBarParams = appBarLayout.layoutParams
        appBarParams.height = actionBarHeight
    }

    fun setupAppBarScrollBehavior(appBarLayout: AppBarLayout, toolbar: MaterialToolbar, searchInput: TextView?, nestedScrollView: NestedScrollView) {
        val typedValue = TypedValue()
        var actionBarHeight = 0
        if(theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight =TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        }

        appBarLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, android.R.color.white))
        appBarLayout.background.alpha = 0

        nestedScrollView.apply {
            setOnScrollChangeListener (NestedScrollView.OnScrollChangeListener{ view, scrollX, scrollY, oldScrollX, oldScrollY ->
                val ratio: Float = (Math.min(scrollY, actionBarHeight)).toFloat() / actionBarHeight
                val newAlpha: Int = (ratio * 255).toInt()
                appBarLayout.background.alpha = newAlpha
//                appBarLayout.elevation = (ratio * 10)
                if(scrollY > actionBarHeight){
                    appBarLayout.elevation = 10f
                    searchInput?.setBackgroundResource(R.drawable.input_rounded_border)
                    changeToolbarIcon(ContextCompat.getColor(applicationContext, R.color.primaryColor), toolbar)
                }else {
                    appBarLayout.elevation = 0f
                    searchInput?.setBackgroundResource(R.drawable.input_rounded)
                    changeToolbarIcon(ContextCompat.getColor(applicationContext, R.color.primaryTextColor), toolbar)
                }
            })
        }
    }

    private fun changeToolbarIcon(color: Int, toolbar: MaterialToolbar) {
        toolbar.run {
            val icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_settings)
            icon!!.setTint(color)
            menu.findItem(R.id.menu_settings).setIcon(icon)
            setNavigationIconTint(color)
            setTitleTextColor(color)
            setSubtitleTextColor(color)
            overflowIcon?.setTint(color)
        }
    }

//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController: NavController = findNavController(R.id.mainNavHostFragment)
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//    }
}