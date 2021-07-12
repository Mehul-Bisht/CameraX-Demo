package com.example.cameraxdemo.models

import android.net.Uri

data class FilterItem(
    var filterName: String,
    var filterPreviewUri: Uri
)