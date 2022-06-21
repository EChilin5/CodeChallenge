package com.eachilin.codechallenge

import java.io.Serializable

data class EventResult(
    var events: List<Event>,
) : Serializable

data class Event(
    var id: Int,
    var datetime_utc:String,
    var announce_date: String,
    var datetime_local: String,
    var description: String,
    var performers: List<Performer>,
    var short_title: String,
    var title: String,
    var venue: Venue,
):Serializable

data class Performer(
    var id: Int,
    var image: String,
    var image_attribution: String,
):Serializable


data class Venue(
    var address: String,
    var display_location: String,
    var extended_address: String,
    var name: String,
):Serializable


