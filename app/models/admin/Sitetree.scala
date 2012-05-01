package models.admin

import play.api._
import play.api.mvc._

import controllers.routes._

abstract class MenuItem
case class MenuItemWithTitle(title: String)
case class MenuItemWithRoute(title: String, route: Call) extends MenuItem
case class MenuItemWithIcon(title: String, route: Call, icon: String) extends MenuItem
case class MenuItemWithChildren(title: String, route: Call, icon: String, children: Seq[MenuItem]) extends MenuItem


object SiteMap {

	val items = Seq(
		MenuItemWithTitle("Content"),
		MenuItemWithIcon("Start", controllers.routes.Application.index, "icon-home" ),
		MenuItemWithChildren("Users", controllers.routes.Accounts.index, "icon-user",
			Seq(
				MenuItemWithRoute("Roles", controllers.routes.Roles.index)
			)	
		),
		MenuItemWithTitle("My settings"),
		MenuItemWithIcon("Profile", controllers.routes.Accounts.user, "icon-info-sign" ),
		MenuItemWithIcon("Signout", controllers.routes.Auth.logout, "icon-off" )
	)

}