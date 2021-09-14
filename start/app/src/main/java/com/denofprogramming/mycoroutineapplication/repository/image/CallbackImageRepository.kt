package com.denofprogramming.mycoroutineapplication.repository.image

import android.graphics.Bitmap
import com.denofprogramming.mycoroutineapplication.network.MockNetworkService
import com.denofprogramming.mycoroutineapplication.network.NetworkService
import com.denofprogramming.mycoroutineapplication.network.allImages
import com.denofprogramming.mycoroutineapplication.shared.Resource
import com.denofprogramming.mycoroutineapplication.shared.uilt.logMessage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CallbackImageRepository(
    private val networkService: NetworkService
) {


    private var _count: Int = -1


    fun cancel() {
        networkService.cancel()
    }

    fun fetchImage(
        imageId: String,
        onSuccessCallback: (Bitmap) -> Unit,
        onFailureCallback: (Exception) -> Unit
    ) {
        logMessage("Start fetchImage() downloading...")
        networkService.getImage(imageId, onSuccessCallback, onFailureCallback)
    }

    fun nextImageId(): String {
        _count++
        if (_count > allImages.size - 1) {
            _count = 0
        }
        return _count.toString()
    }


    companion object {

        fun build(networkService: NetworkService): CallbackImageRepository {
            return CallbackImageRepository(networkService)
        }
    }

}