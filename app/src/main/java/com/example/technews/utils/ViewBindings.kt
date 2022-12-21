package com.example.technews.utils

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.technews.R
import com.google.android.material.imageview.ShapeableImageView


@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val uri = imgUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(imageView)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_hide_image)
            .into(imageView)
    } ?: imageView.setImageResource(R.drawable.ic_hide_image)
}

@BindingAdapter("imageUrl")
fun bindImage(roundedImageView: ShapeableImageView, imgUrl: String?) {
    imgUrl?.let {
        val uri = imgUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(roundedImageView)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_hide_image)
            .into(roundedImageView)
    } ?: roundedImageView.setImageResource(R.drawable.ic_hide_image)
}