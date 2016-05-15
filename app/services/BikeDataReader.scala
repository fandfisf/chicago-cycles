package services

import javax.inject.{Inject, Singleton}

import play.api.libs.ws.WSClient
import play.api.{Configuration, Logger}
/**
  * Created by Prashant S Khanwale @ Suveda LLC  on 5/15/16.
  */
trait BikeDataReader{
  def pullData (): Unit
}
@Singleton
class BikeDataReaderImpl @Inject()(ws:WSClient, configuration:Configuration) extends BikeDataReader{
  def pullData (): Unit ={
      Logger.info(s"Pulling data from ${configuration.getString("elasticSearch.inputDataUrl").getOrElse("URL not specified") }")
//    configuration.getString("elasticSearch.inputDataUrl") match {
//      case Some(url) => {
//        val futureResult: Future[List[Station]] = ws.url(url).get().map {
//          response =>
//            (response.json \ "stationBeanList" ).as[List[Station]]
//        }
//      }
    }


}
