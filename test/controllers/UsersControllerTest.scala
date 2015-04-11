package controllers

import model.User
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{DatabaseTestSetup, UsersRepositoryForTest}

class UsersControllerTest extends WordSpec with Matchers with ScalaFutures with BeforeAndAfterEach with DatabaseTestSetup {

	override val databaseName = "testporrify"
	val usersRepository = UsersRepositoryForTest
	val controller = new UsersController {
		override val repository = usersRepository
	}
	val user = User(None, username = "pepes", email = "pepes@gmail.com", password = "password")

	override protected def beforeAll() {
		super.beforeAll()
		usersRepository.createTable()
	}

	override protected def beforeEach() {
		super.beforeEach()
		usersRepository.removeAllRows()
	}


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
			contentAsString(result) should include("error.users.password.no_match")
		}

		"render an error message if the username already exists in the database" in {
			usersRepository.storeUserInDb(user)
			val result = submitUserDetails(username = user.username)
			status(result) shouldBe 400
			contentAsString(result) should include("error.users.username.not_available")
		}

		"create a new user" in {
			val result = submitUserDetails()
			status(result) shouldBe 200
			contentAsString(result) should include("users.successful_creation")
			usersRepository.findByUsername(user.username) shouldBe 'defined
		}
	}


	def submitUserDetails(username: String = user.username, email: String = user.email, password1: String = user.password, password2: String = user.password) = {
		val request = FakeRequest().withFormUrlEncodedBody(
			"username" -> username,
			"email" -> email,
			"password1" -> password1,
			"password2" -> password2
		)
		controller.createUser.apply(request)
	}

}
