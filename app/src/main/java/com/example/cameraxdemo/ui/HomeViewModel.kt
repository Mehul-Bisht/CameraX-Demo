package com.example.cameraxdemo.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.cameraxdemo.util.Constants.SAVED_IMAGES_DIR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.*

class HomeViewModel: ViewModel() {

    private val _savedImagesUri: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val savedImagesUri: StateFlow<List<Uri>> get() = _savedImagesUri

    fun getUris(context: Context) {

        val sortedFlow = _savedImagesUri.value.sorted()
        val sortedUri = getSavedImagesUriList(context).sorted()

        if (sortedFlow != sortedUri) {

            _savedImagesUri.value = sortedUri
        }
    }

    private fun getSavedImagesUriList(context: Context): List<Uri> {

        val savedImagesUriList = mutableListOf<Uri>()
        val savedImagesDirPath = context.getExternalFilesDir(SAVED_IMAGES_DIR)?.path + "/"
        val savedImagesDir = File(savedImagesDirPath)

        savedImagesDir.listFiles()?.let { savedImagesFileList ->
            if(savedImagesFileList.isNotEmpty()) {

                for(imageFile in savedImagesFileList) {
                    savedImagesUriList.add(Uri.fromFile(imageFile))
                }
            }
        }
        return savedImagesUriList.toList()
    }
}