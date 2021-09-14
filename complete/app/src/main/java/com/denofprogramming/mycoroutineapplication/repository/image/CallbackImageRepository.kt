package com.denofprogramming.mycoroutineapplication.repository.image

import android.graphics.Bitmap
import com.denofprogramming.mycoroutineapplication.network.NetworkService
import com.denofprogramming.mycoroutineapplication.network.allImages
import com.denofprogramming.mycoroutineapplication.shared.Resource
import com.denofprogramming.mycoroutineapplication.shared.uilt.logMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CallbackImageRepository(
    private val networkService: NetworkService
) {


    private var _count: Int = -1

    private var _job = Job()


    fun cancel() {
        _job.cancel()
        _job = Job()
    }


    suspend fun fetchImage(imageId: String): Resource<Bitmap> =
        withContext(Dispatchers.Default + _job) {
            suspendCancellableCoroutine { cancellableContinuation ->

                cancellableContinuation.invokeOnCancellation {
                    logMessage("start invokeOnCancellation")
                    networkService.cancel()
                }

                logMessage("Start fetchImage()")
                networkService.getImage(imageId,
                    onSuccess = { image ->
                        val imageResource = Resource.success(image)
                        logMessage("fetchImage() onSuccess ... $imageResource")
                        cancellableContinuation.resume(imageResource)
                    },
                    onFailure = { e ->
                        logMessage("fetchImage() onFailure ... $e")
                        cancellableContinuation.resumeWithException(e)
                    })
            }
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