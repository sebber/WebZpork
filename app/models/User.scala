package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import models._

case class User(id: Pk[Long], username: String, password: String) {
	lazy val roles = {
		User.getRoles(id.get)
	}
}

object User {

	// --- Parsers
	
	val simple = {
		get[Pk[Long]]("user.id") ~
		get[String]("user.username") ~
		get[String]("user.password") map {
			case id~username~password => User(id, username, password)
		}
	}
	

	// --- Finders

	def findById(id: Long): Option[User] = {
		DB.withConnection { implicit connection =>
			SQL("select * from user where id = {id}").on('id -> id).as(User.simple.singleOpt)
		}
	}

	def findByUsername(username: String): Option[User] = {
		DB.withConnection { implicit connection =>
			SQL("select * from user where username = {username}").on('username -> username).as(User.simple.singleOpt)
		}
	}

	def findAll: List[User] = {
		DB.withConnection { implicit connection =>
			SQL("select * from user").as(User.simple *)
		}
	}


	// --- Business

	def connect(username: String, password: String): Option[User] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from user
					where username = {username}
					and password = {password}
				"""
			).on(
				'username -> username,
				'password -> password
			).as(User.simple.singleOpt)
		}
	}

	def addRole(userId: Long, roleId: Long) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					insert into user_role(user_id, role_id) values(
						{user_id}, {role_id}
					)
				"""
			).on(
				'user_id -> userId,
				'role_id -> roleId
			).executeUpdate()
		}
	}

	def addRoles(userId: Long, roleIds: List[Long]) = {
		flushRoles(userId)
		roleIds foreach ((id: Long) => User.addRole(userId, id))
	}

	def getRoles(userId: Long): Seq[Role] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * 
					from role
					inner join user_role 
					on user_role.user_id = {user_id}
					and	role.id = user_role.role_id
				"""
			).on(
				'user_id -> userId
			).as(Role.simple *)
		}
	}

	def flushRoles(userId: Long) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					delete user_role
					where user_id = {user_id}
				"""
			).on(
				'user_id -> userId
			).executeUpdate()
		}
	}


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


	// --- CUD, Yah without the R because it is under Finders


	/**
	 * Update user
	 *
	 * @param id The user id
	 * @param user The user values
	 */
	def update(id: Long, user: User) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					update user
					set username = {username}
					where id = {id} 
				"""
			).on(
				'id -> id,
				'username -> user.username
			).executeUpdate()
		}
	} 

	/**
	 * Inset a new user
	 * 
	 * @param user The user values
	 */
	def insert(user: User) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				insert into user(username, password) values(
					{username}, {password}
				)
				"""
			).on(
				'username -> user.username,
				'password -> user.password
			).executeUpdate()
		}
	}

}