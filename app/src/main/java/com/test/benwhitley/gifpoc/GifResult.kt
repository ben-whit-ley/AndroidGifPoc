package com.test.benwhitley.gifpoc

data class GifResult(
    val `data`: List<Data>
)

data class Data(
        val images: Images
)

data class Images(
        val original: Original
)

data class Original(
        val url: String
)