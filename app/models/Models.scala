package models

import java.util.Date

import play.api.libs.json.Json

/**
  * Created by Prashant S Khanwale @ Suveda LLC  on 5/15/16.
  */
case class Station(id:Int,stationName:String,availableDocks:Int,totalDocks:Int,latitude:Double,longitude:Double,statusValue:String,statusKey:Int,availableBikes:Int,stAddress1:String,stAddress2:Option[String],city:String,postalCode:Option[String],location:Option[String],altitude:Option[Double],testStation:Boolean,lastCommunicationTime:Option[Date],landMark:Option[String],renting:Boolean,is_renting:Boolean)
object  Station {
  val stationWrites = Json.writes[Station]
  val stationReads = Json.reads[Station]
}
