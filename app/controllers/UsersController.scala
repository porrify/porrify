package controllers

import model.User
import org.apache.commons.lang3.StringUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import repository.UsersRepository

trait UsersController extends Controller {

	def repository: UsersRepository

	val form = Form(
		tuple(
			"username" -> nonEmptyText,
			"email" -> email,
			"password1" -> nonEmptyText,
			"password2" -> nonEmptyText
		)
		.verifying("error.users.password.no_match", data => validatePasswords(data._3, data._4))
		.verifying("error.users.username.not_available", data => validateUsername(data._1))
	)

	private def validatePasswords(password1: String, password2: String): Boolean = StringUtils.equals(password1, password2)

	private def validateUsername(username: String): Boolean = repository.findByUsername(username).isEmpty


	def renderUserCreationForm() = Action {
		Ok(views.html.users.users_form(form))
	}

	def createUser = Action {
		implicit request =>
			form.bindFromRequest.fold(
				formWithErrors =>
					BadRequest(views.html.users.users_form(formWithErrors)),
				data => {
					repository.storeUserInDb(User(data))
					Ok("users.successful_creation")
				}
			)
	}

}

object UsersController extends UsersController {
	override def repository = UsersRepository
}
