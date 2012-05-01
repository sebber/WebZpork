package models

import play.api._
import play.api.mvc._

import controllers.routes._

object SiteMap {

	/*
	 *	MenuItems-collection.
	 *	1. Key
	 *	2. Title
	 *	3. Route 	(optional)
	 *	4. Icon, bootstrap (optional)
	 */

	val items = Seq(
		("main", "Content"),
		("start", "Start", controllers.routes.Application.index, "icon-home" ),
		("users", "Users", controllers.routes.Accounts.index, "icon-user",
			("roles", "Roles", controllers.routes.Roles.index)
		),
		("my-settings", "My settings"),
		("profile", "Profile", controllers.routes.Accounts.user, "icon-info-sign" ),
		("signout", "Signout", controllers.routes.Auth.logout, "icon-off" )
	)
}