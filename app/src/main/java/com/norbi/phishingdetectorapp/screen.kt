package com.norbi.phishingdetectorapp

sealed class Screen{
    data object Menu         : Screen()
    data object CheckSms         : Screen()
    data object CheckPhone         : Screen()
    data object CheckContent         : Screen()
}