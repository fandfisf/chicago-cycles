package controllers

import javax.inject.Inject

import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import services.BikeDataReader

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

  def submit = Action { implicit request =>
    val (street, crossStreet) = form.bindFromRequest.get
    Ok("Looking for bike shop near %s %s".format(street, crossStreet))
  }
}

