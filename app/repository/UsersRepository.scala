package repository

import model.User
import play.api.Play

import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._

trait UsersRepository {

	class Users(tag: Tag) extends Table[User](tag, "USERS") {

		def id = column[Int]("ID", O.PrimaryKey)

		def username = column[String]("USERNAME")

		def email = column[String]("EMAIL")

		def password = column[String]("PASSWORD")

		def * = (id, username, email, password) <>(User.tupled, User.unapply)
	}

	protected def db: H2Driver.simple.Database

	protected val usersRepository = H2Driver.simple.TableQuery[Users]

	def findByUsername(username: String): Option[User] = {
		db.withSession {
			implicit session =>
				usersRepository.filter(_.username === username).firstOption
		}
	}
}

object UsersRepository extends UsersRepository {

	private def readString(key: String) = {
		import play.api.Play.current
		Play.configuration.getString(key).getOrElse(s"$key not defined")
	}

	override protected val db: H2Driver.backend.Database = {
		Database.forURL(
			url = readString("db.porrify.url"),
			driver = readString("db.porrify.driver"),
			user = readString("db.porrify.user"),
			password = readString("db.porrify.password")
		)
	}
}

