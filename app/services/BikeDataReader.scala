package services

import javax.inject.{Inject, Singleton}

import models.Count._
import models.Station
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.{Configuration, Logger}

import scala.concurrent.ExecutionContext

/**
  * Created by Prashant S Khanwale @ Suveda LLC  on 5/15/16.
  */
trait BikeDataReader {
  def checkData(): Boolean

  def pullData(): Boolean
}

@Singleton
class BikeDataReaderImpl @Inject()(ws: WSClient, configuration: Configuration)(implicit exec: ExecutionContext) extends BikeDataReader {
  def checkData(): Boolean = {
    Logger.info(s"Pulling data from ${configuration.getString("elasticSearch.inputDataUrl").getOrElse("URL not specified")}")
    configuration.getString("elasticSearch.indexName") match {
      case Some(indexName) =>
        configuration.getString("elasticSearch.baseUrl") match {
          case Some(url) => {
            ws.url(url + "/" + indexName + "/_count").get().map {
              response =>
                Logger.info(s"Local elastic search response [${
                  response.body
                }]")
                countReads.reads(response.json).asOpt match {
                  case Some(c) => {
                    Logger.info(s"Found ${c.count} occurrences")
                    return true
                  }
                  case _ => Logger.info(s"Index ${indexName} not found.")
                }
            }
          }
        }
    }
    false
  }

  def pullData():Boolean = {
    val json :JsValue = Json.parse (
      """
        |{"executionTime":"2016-05-15 01:44:31 PM","stationBeanList":[{"id":2,"stationName":"Buckingham Fountain","availableDocks":26,"totalDocks":35,"latitude":41.876428,"longitude":-87.620339,"statusValue":"In Service","statusKey":1,"availableBikes":9,"stAddress1":"Buckingham Fountain","stAddress2":"","city":"Chicago","postalCode":"","location":"","altitude":"","testStation":false,"lastCommunicationTime":null,"landMark":"541","renting":true,"is_renting":true},{"id":3,"stationName":"Shedd Aquarium","availableDocks":27,"totalDocks":31,"latitude":41.86722595682,"longitude":-87.6153553902,"statusValue":"In Service","statusKey":1,"availableBikes":4,"stAddress1":"Shedd Aquarium","stAddress2":"","city":"Chicago","postalCode":"","location":"","altitude":"","testStation":false,"lastCommunicationTime":null,"landMark":"544","renting":true,"is_renting":true}]}
      """.stripMargin)
    val sb = (json \ "stationBeanList")
    Logger.info(s"First station is ${sb(0)}, second is ${sb(1)} the total is --- \n ${sb.as[JsArray].value.size}")//and there are a total of ${sb.as[JsArray].value.size} stations.")
    val stations = (json \ "stationBeanList").as[Seq[Station]]
    Logger.info(s"From stations SEQ - First station is ${stations(0)}, second is ${stations(1)} the total is --- \n ${stations.size}")//and there are a total of ${sb.as[JsArray].value.size} stations.")
    false
  }

  def pull1Data(): Boolean = {
    Logger.info(s"Pulling data from ${configuration.getString("elasticSearch.inputDataUrl").getOrElse("URL not specified")}")
    configuration.getString("elasticSearch.inputDataUrl") match {
      case Some(url) => {
        ws.url(url).get().map {
          response =>
            Logger.info(s"converting json response to JSON objects.")
//            val stationList = (response.json\"stationBeanList").as[JsArray[Station]]
//            Logger.info(s"Chicago bike data [${stationList}]")

        }
      }
    }
    false
  }
}

//  (response.json \ "stationBeanList" ).as[List[Station]]
