package controllers

import model.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

trait UsersController extends Controller {

	val form = Form(
		tuple(
			"username" -> text,
			"email" -> text,
			"password1" -> text,
			"password2" -> text
		)
	)

	def renderUserCreationForm() = Action {
		Ok(views.html.users.users_form(form))
	}

	def createUser = Action {
		implicit request =>
			form.bindFromRequest.fold(
				formWithErrors =>
					Ok(views.html.users.users_form(form)),
				user =>
					Ok("blah")
			)
	}

}

object UsersController extends UsersController
