package com.denofprogramming.mycoroutineapplication.network

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.denofprogramming.mycoroutineapplication.shared.uilt.logMessage

class MockNetworkService : NetworkService {


    private var thread: Thread? = null
    private var cancelled = false


    override fun cancel() {
        logMessage("MockNetworkService.cancel() cancelling download!")
        thread?.interrupt()
    }

    //Mock Network library not supporting Coroutines... simulates Callback, runs on separate Thread
    override fun getImage(id: String, onSuccess: (Bitmap) -> Unit, onFailure: (Exception) -> Unit) {
        thread = Thread {
            try {
                logMessage("MockNetworkService.getImage() downloading...")
                Thread.sleep(5000) // Simulate network call and download...
                logMessage("MockNetworkService.getImage() download complete!")
                //throw IOException("Ouchee!!") //example exception
                val image = allImages[id.toInt()]
                //Uses Looper to Post onto the Main Thread.
                Handler(Looper.getMainLooper()).post {
                    onSuccess(image)
                }
            } catch (e: InterruptedException) {
                cancelled = true
                logMessage("*** Downloaded cancelled ***")
                Handler(Looper.getMainLooper()).post {
                    onFailure(e)
                }
            } catch (e: Exception) {
                //Uses Looper to Post onto the Main Thread.
                Handler(Looper.getMainLooper()).post {
                    onFailure(e)
                }
            } finally {
                cancelled = false
            }
        }
        thread?.start()
    }


    companion object {

        fun build(): NetworkService {
            return MockNetworkService()
        }

    }

}