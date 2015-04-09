package controllers

import org.apache.commons.lang3.StringUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

trait UsersController extends Controller {

	val form = Form(
		tuple(
			"username" -> nonEmptyText,
			"email" -> email,
			"password1" -> nonEmptyText,
			"password2" -> nonEmptyText
		).verifying("error.users.invalid_passwords", data => validatePasswords(data))
	)

	private def validatePasswords(data: (String, String, String, String)): Boolean = {
		val password1 = data._3
		val password2 = data._4
		StringUtils.equals(password1, password2)
	}

	def renderUserCreationForm() = Action {
		Ok(views.html.users.users_form(form))
	}

	def createUser = Action {
		implicit request =>
			form.bindFromRequest.fold(
				formWithErrors =>
					BadRequest(views.html.users.users_form(formWithErrors)),
				user =>
					Ok("blah")
			)
	}

}

object UsersController extends UsersController
