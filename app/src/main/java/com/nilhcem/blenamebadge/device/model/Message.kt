package com.nilhcem.blenamebadge.device.model

data class Message(val hexStrings: List<String>, val flash: Boolean = false, val marquee: Boolean = false, val speed: Speed = Speed.ONE, val mode: Mode = Mode.LEFT)