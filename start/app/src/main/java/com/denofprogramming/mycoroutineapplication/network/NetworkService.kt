package com.denofprogramming.mycoroutineapplication.network

import android.graphics.Bitmap
import java.lang.Exception

interface NetworkService {


    fun cancel()

    fun getImage(id: String, onSuccess: (Bitmap) -> Unit, onFailure: (Exception) -> Unit)

}