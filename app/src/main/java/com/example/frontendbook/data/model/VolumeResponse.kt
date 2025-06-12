package com.example.frontendbook.data.model

import com.example.frontendbook.domain.model.googleapi.VolumeInfo

data class VolumeResponse(
    val items: List<VolumeItem>?
)

data class VolumeItem(
    val volumeInfo: VolumeInfo
)




