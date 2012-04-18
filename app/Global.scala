import play.api.Play.current
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

import play.api.data._

import anorm._

import models._

import play.api.cache._

object Global extends GlobalSettings {

	override def onStart(app: Application) {
		Logger.info("Application has started")

		setupUsers
		setupRoles
	}



	def setupUsers = {
		if(User.findAll.isEmpty) {
			Logger.info("userlist is empty")

            Seq(
                User(NotAssigned, "basse", "hemligt")
            ).foreach(User.insert)

            Logger.info("But not anymore!")

		} else {
			Logger.info("userlist isn't empty")
		}
	}

	def setupRoles = {
		if(Role.findAll.isEmpty) {
			Logger.info("No roles exists")

			Seq(
				Role(NotAssigned, "Guest", "The most simple role, almost no permissions"),
				Role(NotAssigned, "User", "For simple logginable users"),
				Role(NotAssigned, "Admin", "Administrative superuser")
			).foreach(Role.insert)


			Logger.info("But now they do... a couple.")

		} else {
			Logger.info("Some roles does already exist.")
		}
	}

}