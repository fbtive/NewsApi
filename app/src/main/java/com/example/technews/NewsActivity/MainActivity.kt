package com.example.technews.NewsActivity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ScrollView
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
import androidx.recyclerview.widget.RecyclerView
import com.example.technews.R
import com.example.technews.databinding.ActivityMainBinding
import com.example.technews.newsheadlines.HeadlineFragmentDirections
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = findNavController(R.id.mainNavHostFragment)

        appBarConfiguration = AppBarConfiguration
            .Builder(R.id.headline_fragment_dst, R.id.sources_fragment_dst, R.id.bookmark_fragment_dst)
            .build()

        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        NavigationUI.setupWithNavController(binding.mainToolbar, navController, appBarConfiguration)

//        val drawable: MaterialShapeDrawable = binding.mainToolbar.background as MaterialShapeDrawable
//        drawable.shapeAppearanceModel = drawable.shapeAppearanceModel
//            .toBuilder()
//            .setAllCorners(CornerFamily.ROUNDED, 60f)
//            .build()


        binding.mainToolbar.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
                R.id.menu_settings -> {
                    navController.navigate(R.id.articleFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            when (nd.id) {
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
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
//
//    fun recalculateToolbarAndRootView(view: View, appBarLayout: AppBarLayout) {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        ViewCompat.setOnApplyWindowInsetsListener(view) { root, windowInset ->
//            val inset = windowInset.getInsets(WindowInsetsCompat.Type.systemBars())
//            root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                leftMargin = inset.left
//                rightMargin = inset.right
//                bottomMargin = inset.bottom
//                topMargin = 0
//            }
//
//            WindowInsetsCompat.CONSUMED
//        }
//
//        var statusBarHeight = 0
//        var resourceID = resources.getIdentifier("status_bar_height", "dimen", "android")
//        if (resourceID > 0) {
//            statusBarHeight = resources.getDimensionPixelSize(resourceID)
//        }
//
//        val typedValue = TypedValue()
//        var actionBarHeight = 0
//        if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
//            actionBarHeight =
//                TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
//        }
//        actionBarHeight += statusBarHeight
//
//        val appBarParams = appBarLayout.layoutParams
//        appBarParams.height = actionBarHeight
//    }
//
//    fun setupAppBarScrollBehavior(
//        appBarLayout: AppBarLayout,
//        toolbar: MaterialToolbar,
//        searchInput: TextView?,
//        nestedScrollView: NestedScrollView
//    ) {
//        var statusBarHeight = 0
//        var resourceID = resources.getIdentifier("status_bar_height", "dimen", "android")
//        if (resourceID > 0) {
//            statusBarHeight = resources.getDimensionPixelSize(resourceID)
//        }
//
//        val typedValue = TypedValue()
//        var actionBarHeight = 0
//        if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(
//                typedValue.data,
//                resources.displayMetrics
//            ) + statusBarHeight
//        }
//
//        appBarLayout.setBackgroundColor(
//            ContextCompat.getColor(
//                applicationContext,
//                R.color.primaryColor
//            )
//        )
//        appBarLayout.background.alpha = 255
//
//        nestedScrollView.apply {
//            setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//                val ratio: Float = (Math.min(scrollY, actionBarHeight)).toFloat() / actionBarHeight
//                val newAlpha: Int = (ratio * 255).toInt()
//                appBarLayout.background.alpha = newAlpha
//                //                appBarLayout.elevation = (ratio * 10)
//                if (scrollY > actionBarHeight) {
//                    appBarLayout.elevation = 10f
//                    searchInput?.setBackgroundResource(R.drawable.input_rounded_border)
//                    //                    changeToolbarIcon(ContextCompat.getColor(applicationContext, R.color.primaryColor), toolbar)
//                } else {
//                    appBarLayout.elevation = 0f
//                    searchInput?.setBackgroundResource(R.drawable.input_rounded)
//                    //                    changeToolbarIcon(ContextCompat.getColor(applicationContext, R.color.primaryTextColor), toolbar)
//                }
//            })
//        }
//    }
//
//    private fun changeToolbarIcon(color: Int, toolbar: MaterialToolbar) {
//        toolbar.run {
//            val icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_settings)
//            icon!!.setTint(color)
//            menu.findItem(R.id.menu_settings).setIcon(icon)
//            setNavigationIconTint(color)
//            setTitleTextColor(color)
//            setSubtitleTextColor(color)
//            overflowIcon?.setTint(color)
//        }
//    }

}