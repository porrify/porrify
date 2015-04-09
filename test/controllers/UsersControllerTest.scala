package controllers

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._

class UsersControllerTest extends WordSpec with Matchers with ScalaFutures {

	"Creating a new user" should {

		"render an error message if the username is not provided" in {
			val result = submitUserDetails(username = "")
			status(result) shouldBe 400
			contentAsString(result) should include("error.required")
		}

		"render an error message if the email is not provided" in {
			val result = submitUserDetails(email = "")
			status(result) shouldBe 400
			contentAsString(result) should include("error.email")
		}

		"render an error message if the email is not a valid email address" in {
			val result = submitUserDetails(email = "not_an_email")
			status(result) shouldBe 400
			contentAsString(result) should include("error.email")
		}

		"render an error message if the password is not provided" in {
			val result = submitUserDetails(password1 = "")
			status(result) shouldBe 400
			contentAsString(result) should include("error.required")
		}

		"render an error message if the second password is not provided" in {
			val result = submitUserDetails(password2 = "")
			status(result) shouldBe 400
			contentAsString(result) should include("error.required")
		}

		"render an error message if the two passwords provided are not the same" in {
			val result = submitUserDetails(password1 = "aaaa", password2 = "bbbb")
			status(result) shouldBe 400
			contentAsString(result) should include("error.users.invalid_passwords")
		}

		"render an error message if the username already exists in the database" in {

		}

		"create a new user" is pending
	}

	def submitUserDetails(username: String = "pepes", email: String = "pepes@gmail.com", password1: String = "password", password2: String = "password") = {
		val request = FakeRequest().withFormUrlEncodedBody(
			"username" -> username,
			"email" -> email,
			"password1" -> password1,
			"password2" -> password2
		)
		UsersController.createUser.apply(request)
	}
}
