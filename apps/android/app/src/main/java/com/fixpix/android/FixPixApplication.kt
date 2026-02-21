package com.fixpix.android

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FixPixApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            // Critical for editing: 
            // If we are just displaying, hardware is fine. 
            // If we need to read pixels for the editor canvas, we might need software.
            // For general UI, hardware is faster. We'll default to true here, 
            // and override to false specifically in the Editor Canvas request.
            .allowHardware(true) 
            .build()
    }
}
