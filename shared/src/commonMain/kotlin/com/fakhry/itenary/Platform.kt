package com.fakhry.itenary

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform