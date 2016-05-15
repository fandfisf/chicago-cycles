package controllers

import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

class LocationSearch extends Controller {
  val form = Form(
    tuple(
      "street" -> text,
      "crossStreet" -> text
    )
  )

  def index = Action {
    Ok(views.html.search())
  }

  def submit = Action { implicit request =>
    val (street, crossStreet) = form.bindFromRequest.get
    Ok("Looking for bike shop near %s %s".format(street, crossStreet))
  }
}

