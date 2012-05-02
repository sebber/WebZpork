package models.game

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import models._

case class PlayerProfile(id: Pk[Long], user_id: Long, name: String, about: String)  {

	lazy val user: User = User.findById(user_id).get

}

object Player {

	// --- Parsers
	
	val simple = {
		get[Pk[Long]]("player.id") ~
		get[Long]("player.user_id") ~
		get[String]("player.name") ~
		get[String]("player.about") map {
			case id~user_id~name~about => PlayerProfile(id, user_id, name, about)
		}
	}
	

	// --- Finders

	def findById(id: Long): Option[PlayerProfile] = {
		DB.withConnection { implicit connection =>
			SQL("select * from player where id = {id}").on('id -> id).as(Player.simple.singleOpt)
		}
	}



}