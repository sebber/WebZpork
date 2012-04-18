package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import models._

case class Role(id: Pk[Long], title: String, description: String)

object Role {

	// --- Parsers
	
	val simple = {
		get[Pk[Long]]("role.id") ~
		get[String]("role.title") ~
		get[String]("role.description") map {
			case id~title~description => Role(id, title, description)
		}
	}
	

	// --- Finders

	def findById(id: Long): Option[Role] = {
		DB.withConnection { implicit connection =>
			SQL("select * from role where id = {id}").on('id -> id).as(Role.simple.singleOpt)
		}
	}

	def findAll: List[Role] = {
		DB.withConnection { implicit connection =>
			SQL("select * from role").as(Role.simple *)
		}
	}

/*

	/**
	 * Return a page of Users
	 * @param page Page to display
	 * @param pageSize Number of users to display
	 * @param orderBy Which property to sort by
	 * @param filter Filter by  
	 */
	def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Seq[User] = {
		
		val offset = pageSize * page

		DB.withConnection { implicit connection =>

			/*
				When users have emails, add this to filtering:

				SQL:
	 			where user.email like {filter}
	 			or user.email like {filter}
			*/

			SQL(
	 		"""
	 			select * from user
	 			where user.username like {filter}
	 			order by {orderBy} nulls last
	 			limit {pageSize} offset {offset}
	 		"""
		 	).on(
				'pageSize -> pageSize,
				'offset -> offset,
				'filter -> filter,
				'orderBy -> orderBy 
			).as(User.simple *)
		}
	}
*/

	// --- CRUD

	/**
	 * Update role
	 *
	 * @param id The role id
	 * @param role The role values
	 */
	def update(id: Long, role: Role) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					update role
					set title = {title}
					and description = {description}
					where id = {id} 
				"""
			).on(
				'id -> id,
				'title -> role.title,
				'description -> role.description
			).executeUpdate()
		}
	} 


	/**
	 * Inset a new role
	 * 
	 * @param role The Role values
	 */
	def insert(role: Role) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				insert into role(title, description) values(
					{title}, {description}
				)
				"""
			).on(
				'title -> role.title,
				'description -> role.description
			).executeUpdate()
		}
	}

}