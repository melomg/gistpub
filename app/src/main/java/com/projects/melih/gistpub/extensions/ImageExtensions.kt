package com.projects.melih.gistpub.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.projects.melih.gistpub.R

/**
 * Created by melih on 15.10.2017
 */

fun ImageView.loadImage(url: String, listener: ImageListener) {
    Glide.with(context)
            .load(url)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<String, Bitmap> {
                override fun onException(e: Exception, model: String, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                    listener.hide()
                    return false
                }

                override fun onResourceReady(resource: Bitmap, model: String, target: Target<Bitmap>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    listener.hide()
                    return false
                }
            })
            .centerCrop()
            .dontAnimate()
            .thumbnail(0.1f)
            .placeholder(R.color.gray_light)
            .into(this)
}

interface ImageListener {
    fun hide()
}