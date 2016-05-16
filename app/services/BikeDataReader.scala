package services

import javax.inject.{Inject, Singleton}

import com.google.inject.Provider
import models.Count._
import models.Station
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.{Application, Configuration, Logger}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Prashant S Khanwale @ Suveda LLC  on 5/15/16.
  */
trait BikeDataReader {
  def checkData(): Boolean

  def pullData(): Boolean

  def queryData(phrase:String):Future[String]
}

@Singleton
class BikeDataReaderImpl @Inject()(appProvider: Provider[Application],ws: WSClient, configuration: Configuration)(implicit exec: ExecutionContext) extends BikeDataReader {
  implicit lazy val app = appProvider.get()
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
    val json :JsValue  =   Json.parse(app.resourceAsStream("divvy-data.json").get)
    val sb = (json \ "stationBeanList")
    Logger.info(s"First station is ${sb(0)}, second is ${sb(1)} the total is --- \n ${sb.as[JsArray].value.size}")
    val stations = (json \ "stationBeanList").as[Seq[Station]]
    Logger.info(s"From stations SEQ - First station is ${stations(0)}, second is ${stations(1)} the total is --- \n ${stations.size}")
    stations.foreach(station => ws.url(s"http://localhost:9200/chicago-city-bike-data/station/${station.id}").put(Json.writes[Station].writes(station)))
    true
  }

  def queryData (phrase:String):Future[String] = {
    ws.url(s"http://localhost:9200/chicago-city-bike-data/station/_search").post(
      s"""
        |{
        |  "query" : {
        |      "match_phrase" : {
        |          "stAddress1" : "${phrase}"
        |      }
        |  }
        |}
      """.stripMargin).map ({
      _.body
      })
  }
  def pullRealData(): Boolean = {
    Logger.info(s"Pulling data from ${configuration.getString("elasticSearch.inputDataUrl").getOrElse("URL not specified")}")
    configuration.getString("elasticSearch.inputDataUrl") match {
      case Some(url) => {
        ws.url(url).get().map ({
          response =>
            Logger.info(s"converting json response to JSON objects.")
            //Do stuff with the data
        })
      }
    }
    false
  }
}

