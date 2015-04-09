package repository

import model.User
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpec}

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

class UsersRepositoryTest extends WordSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach with UsersRepositoryUnderTest {

	override protected def beforeAll() {
		db.withSession {
			implicit session =>
				if (MTable.getTables.list.filter(_.name.name === "USERS").isEmpty) {
					usersRepository.ddl.create
				}
		}
	}

	override protected def beforeEach {
		removeAllRows()
	}

	"Users repository - Find by username" should {

		"return None if the username is not found" in new UsersRepositoryUnderTest {
			findByUsername("username_not_found") shouldBe None
		}

		"return the User if the username is found" in new UsersRepositoryUnderTest {
			val username = "username"
			val user = User(1246, username, "email@email.com", "1234")
			storeUserInDb(user)

			val result = findByUsername(username)
			result shouldBe 'defined
			result.get shouldBe user
		}
	}
}

trait UsersRepositoryUnderTest extends UsersRepository {

	private val url = "jdbc:h2:~/testporrify"
	private val driver = "org.h2.Driver"
	private val user = "testPorrify"
	private val password = "testPassword"

	override protected def db = Database.forURL(url, driver = driver, user = user, password = password)

	def storeUserInDb(user: User) {
		db.withSession {
			implicit session =>
				usersRepository.insert(user)
		}
	}

	def removeAllRows() {
		db.withSession {
			implicit session =>
				usersRepository.delete
		}
	}
}