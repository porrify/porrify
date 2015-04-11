package repository

import model.User
import play.api.Play

import scala.slick.driver.MySQLDriver
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

trait UsersRepository {

	def tableName: String

	def db: MySQLDriver.simple.Database

	val usersRepository = MySQLDriver.simple.TableQuery[Users]

	def createTable() {
		db.withSession {
			implicit session =>
				MTable.getTables.list.foreach(t => println(t.name.name))
				if (MTable.getTables.list.filter(_.name.name == tableName).isEmpty) {
					usersRepository.ddl.create
				}
		}
	}

	def findByUsername(username: String): Option[User] = {
		db.withSession {
			implicit session =>
				usersRepository.filter(_.username === username).firstOption
		}
	}

	def storeUserInDb(user: User) {
		db.withSession {
			implicit session =>
				usersRepository.insert(user)
		}
	}

	class Users(tag: Tag) extends Table[User](tag, tableName) {

		def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

		def username = column[String]("USERNAME")

		def email = column[String]("EMAIL")

		def password = column[String]("PASSWORD")

		def * = (id.?, username, email, password) <> (User.tupled, User.unapply)

	}
}

object UsersRepository extends UsersRepository {

	override val tableName: String = "USERS"

	override val db: MySQLDriver.simple.Database = {
		Database.forURL(
			url = s"${readString("db.default.url")}/porrify",
			driver = readString("db.default.driver"),
			user = readString("db.default.user"),
			password = readString("db.default.password")
		)
	}

	private def readString(key: String) = {
		import play.api.Play.current
		Play.configuration.getString(key).getOrElse(s"$key not defined")
	}
}