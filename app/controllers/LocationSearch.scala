package controllers

import javax.inject.Inject

import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import services.BikeDataReader
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class LocationSearch @Inject()(bdr:BikeDataReader) extends Controller {
  val form = Form(
    tuple(
      "street" -> text,
      "crossStreet" -> text
    )
  )

  def index = Action {
    bdr.pullData()
    Ok(views.html.search())
  }

  def submit = Action.async( { implicit request =>
    val (street, crossStreet) = form.bindFromRequest.get
    val stations = bdr.queryData(street)
    stations.map ( s => Ok("Bike shops near %s %s are %s ".format(street, crossStreet,s)))
  })
}

