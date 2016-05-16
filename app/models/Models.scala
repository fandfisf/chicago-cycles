package models

import java.util.Date

import play.api.libs.json.Json

/**
  * Created by Prashant S Khanwale @ Suveda LLC  on 5/15/16.
  */
case class Station(id:Int,stationName:String,availableDocks:Int,totalDocks:Int,latitude:Double,longitude:Double,statusValue:String,statusKey:Int,availableBikes:Int,stAddress1:String,stAddress2:Option[String],city:String,postalCode:Option[String],location:Option[String],altitude:Option[String],testStation:Boolean,lastCommunicationTime:Option[Date],landMark:Option[String],renting:Boolean,is_renting:Boolean)
object  Station {
  implicit val stationWrites = Json.writes[Station]
  implicit val stationReads = Json.reads[Station]
}
case class Shard (total: Int,successful: Int, failed: Int)
object Shard {
  implicit val shardReads = Json.reads[Shard]
}
case class Count(count:Int, _shards:Shard)
object Count {
  implicit val countReads = Json.reads[Count]
}
