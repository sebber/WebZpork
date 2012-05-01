package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import play.api.cache._

import anorm._

import views._
import models._

object Roles extends Controller with Secured {

  // --- Forms

  val roleForm = Form(
    mapping(
      "id" -> ignored(NotAssigned: Pk[Long]),
      "title" -> nonEmptyText,
      "description" -> text
    )(Role.apply)(Role.unapply)
  )



  // --- Actions
 
  def index = withUser { user => implicit request =>
  	Ok(html.roles.list(Role.list(0, 10, 1)))
  }


  // --- Business

  


  // --- CRUD Actions

/*
  def show(id: Long) = withAuth { user => implicit request =>
		User.findById(id).map {
			showUser => Ok(html.accounts.show(showUser))
		}.getOrElse {
			Forbidden
		}
	}
*/

  def edit(id: Long) = withAuth { user => implicit request =>
  	Role.findById(id).map {
  		role => Ok(html.roles.edit(id, roleForm.fill(role)))	
  	}.getOrElse {
  		NotFound
  	}
  }

  def update(id: Long) = withAuth { user => implicit request =>
    roleForm.bindFromRequest.fold(
		  formWithErrors => BadRequest(html.roles.edit(id, formWithErrors)),
		    role => {
          Role.update(id, role)
          Redirect(routes.Roles.index)
		    }
    )
  }


  def create = withAuth { username => implicit request =>
  	Ok(html.roles.create(roleForm))
  }

  def save = withAuth { user => implicit request =>
  	roleForm.bindFromRequest.fold(
  	  formWithErrors => BadRequest(html.roles.create(formWithErrors)),
  	  role => {
  	  	Role.insert(role)
  	  	Redirect(routes.Roles.index)
  	  }
  	)
  }


  def delete(id: Long) = withAuth { username => implicit request => 
    Role.delete(id)
    Redirect(routes.Roles.index).flashing(
      "success" -> "Role deleted"
    )
  }


}