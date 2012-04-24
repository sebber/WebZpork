import play.api.Play.current
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

import play.api.data._

import anorm._

import models._

import play.api.cache._

object Global extends GlobalSettings {

	override def onRouteRequest(request: RequestHeader): Option[Handler] = {

	    Logger.info("executed before every request:" + request.toString)

	    request.session + ("current_url" -> request.toString)

	    super.onRouteRequest(request)
	}



	override def onStart(app: Application) {
		Logger.info("Application has started")

		setupUsers
		setupRoles
		setupUserWithRoles
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

	def setupUserWithRoles = {
		val users = User.findAll
		val roleUser = Role.findByName("User").get

		users.map { user =>
			if(user.roles.isEmpty) {
				Logger.info("user "+ user.username +" has no roles")

				User.addRole(user.id.get, roleUser.id.get)

				Logger.info("But now he has one :)")
			}
		}

	}


}