package com.developer.map.Model

class Locations {
    var latitude:Double?=null
    var longitude:Double?=null


    constructor(latitude: Double?, longitude: Double?) {
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor()

    override fun toString(): String {
        return "Locations(latitude=$latitude, longitude=$longitude)"
    }

}