package repository

import model.User
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}
import utils.{DatabaseTestSetup, UsersRepositoryForTest}

class UsersRepositoryTest extends WordSpec with Matchers with BeforeAndAfterEach with BeforeAndAfterAll with DatabaseTestSetup {

	val repository = UsersRepositoryForTest

	override protected def beforeAll() {
		super.beforeAll()
		createTestDatabase()
		repository.createTable()
	}

	override protected def beforeEach() {
		super.beforeEach()
		repository.removeAllRows()
	}

	"Users repository - Find by username" should {

		"return None if the username is not found" in {
			repository.findByUsername("username_not_found") shouldBe None
		}

		"return the User if the username is found" in {
			val username = "username"
			val user = User(None, username, "email@email.com", "1234")
			repository.storeUserInDb(user)

			val foundUser = repository.findByUsername(username).get
			foundUser.id shouldBe 'defined
			foundUser should have(
				'username(user.username),
				'email(user.email),
				'password(user.password)
			)
		}
	}
}

