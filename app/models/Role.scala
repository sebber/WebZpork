package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import models._

case class Role(id: Pk[Long], title: String, description: String) {
	override def toString = this.title
}

object Role {

	// --- Parsers
	
	val simple = {
		get[Pk[Long]]("role.id") ~
		get[String]("role.title") ~
		get[String]("role.description") map {
			case id~title~description => Role(id, title, description)
		}
	}
	

	// --- Business

	def hasUser(id: Long, userId: Long): Boolean = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select count(*) 
					from user_role 
					where user_id = {user_id}
					and	role_id = {role_id}
				"""
			).on(
				'user_id -> userId,
				'role_id -> id
			).as(scalar[Long].single) > 0
		}
	}


	// --- Finders

	def findById(id: Long): Option[Role] = {
		DB.withConnection { implicit connection =>
			SQL("select * from role where id = {id}").on('id -> id).as(Role.simple.singleOpt)
		}
	}

	def findByName(name: String): Option[Role] = {
		DB.withConnection { implicit connection =>
			SQL("select * from role where title = {name}").on('name -> name).as(Role.simple.singleOpt)
		}
	}

	def findAll: List[Role] = {
		DB.withConnection { implicit connection =>
			SQL("select * from role").as(Role.simple *)
		}
	}

	/**
	 * Return a page of Users
	 * @param page Page to display
	 * @param pageSize Number of users to display
	 * @param orderBy Which property to sort by
	 * @param filter Filter by  
	 */
	def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Seq[Role] = {
		
		val offset = pageSize * page

		DB.withConnection { implicit connection =>


			SQL(
	 		"""
	 			select * from role
	 			where role.title like {filter}
	 			order by {orderBy} nulls last
	 			limit {pageSize} offset {offset}
	 		"""
		 	).on(
				'pageSize -> pageSize,
				'offset -> offset,
				'filter -> filter,
				'orderBy -> orderBy 
			).as(Role.simple *)
		}
	}


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
					set title = {title},
					description = {description}
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