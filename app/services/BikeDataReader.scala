package services

import javax.inject.{Inject, Singleton}

import models.Count
import models.Count._
import play.api.libs.ws.WSClient
import play.api.{Configuration, Logger}

import scala.concurrent.ExecutionContext

/**
  * Created by Prashant S Khanwale @ Suveda LLC  on 5/15/16.
  */
trait BikeDataReader {
  def pullData(): Unit
}

@Singleton
class BikeDataReaderImpl @Inject()(ws: WSClient, configuration: Configuration)(implicit exec: ExecutionContext) extends BikeDataReader {
  def pullData(): Unit = {
    Logger.info(s"Pulling data from ${configuration.getString("elasticSearch.inputDataUrl").getOrElse("URL not specified")}")
    configuration.getString("elasticSearch.indexName") match {
      case Some(index) =>
        configuration.getString("elasticSearch.baseUrl") match {
          case Some(url) => {
            ws.url(url + "/"+index+"/_count").get().map {
              response =>
                Logger.info(s"Local elastic search response [${
                  response.body
                }]")
                val c:Count = countReads.reads(response.json).asOpt.get
                Logger.info(s"Found ${c.count} occurrences")
            }
          }
        }
    }
  }
}

//  (response.json \ "stationBeanList" ).as[List[Station]]
