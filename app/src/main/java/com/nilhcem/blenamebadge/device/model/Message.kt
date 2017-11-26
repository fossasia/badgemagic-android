package com.nilhcem.blenamebadge.device.model

data class Message(val text: String, val marquee: Boolean = false, val speed: Speed = Speed.ONE, val mode: Mode = Mode.LEFT)
