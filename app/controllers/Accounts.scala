package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import play.api.cache._

import anorm._

import views._
import models._

object Accounts extends Controller with Secured {

  // --- Forms

  val userForm = Form(
    mapping(
	    "id" -> ignored(NotAssigned:Pk[Long]),
	    "username" -> nonEmptyText,
	    "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )

  val rolesForm = Form(
    tuple(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "ids" -> list(longNumber)
    )
  )



  // --- Actions
 
  def index = withUser { user => implicit request =>
  	Ok(html.accounts.list(User.list(0, 10, 1)))
  }

  def user = withUser { user => implicit request =>
  	Ok(html.accounts.show(user))
  }


  // --- Business

  def addRoleToUser(userId: Long) = withUser { user => implicit request =>
    val curr_user = User.findById(userId).get
    val roleIds = rolesForm.bindFromRequest.get._2

    User.addRoles(curr_user.id.get, roleIds)
    Redirect(routes.Accounts.edit(userId))
  }


  // --- CRUD Actions

  def show(id: Long) = withAuth { user => implicit request =>
		User.findById(id).map {
			showUser => Ok(html.accounts.show(showUser))
		}.getOrElse {
			Forbidden
		}
	}


  def edit(id: Long) = withAuth { user => implicit request =>
  	User.findById(id).map {
  		editedUser => Ok(html.accounts.edit(id, userForm.fill(editedUser)))	
  	}.getOrElse {
  		Forbidden
  	}
  }

  def update(id: Long) = withAuth { user => implicit request =>
    userForm.bindFromRequest.fold(
		  formWithErrors => BadRequest(html.accounts.edit(id, formWithErrors)),
		    editedUser => {
          User.update(id, editedUser)
          Redirect(routes.Accounts.show(id))
		    }
    )
  }


  def create = withAuth { username => implicit request =>
  	Ok(html.accounts.create(userForm))
  }

  def save = withAuth { user => implicit request =>
  	userForm.bindFromRequest.fold(
  	  formWithErrors => BadRequest(html.accounts.create(formWithErrors)),
  	  newUser => {
  	  	User.insert(newUser)
  	  	Redirect(routes.Accounts.index)
  	  }
  	)
  }

}