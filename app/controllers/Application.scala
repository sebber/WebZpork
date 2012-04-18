package controllers

import play.api._
import play.api.mvc._

object Application extends Controller with Secured {
  
  def index = Action {
    Ok(views.html.index("Välkommen."))
  }

  def user = withUser { user => implicit request =>
  	Ok(user.username + ":" + user.password)
  }

}