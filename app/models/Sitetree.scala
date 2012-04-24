package models

import play.api._
import play.api.mvc._

import controllers.routes._

object SiteMap {

	val items = Seq(
		("main", "Content"),
		("start", "Start", controllers.routes.Application.index, "icon-home" ),
		("users", "Users", controllers.routes.Accounts.index, "icon-user" ),
		("my-settings", "My settings"),
		("profile", "Profile", controllers.routes.Accounts.user ),
		("signout", "Signout", controllers.routes.Auth.logout )
	)
}