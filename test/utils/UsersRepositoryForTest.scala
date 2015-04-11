package utils

import repository.UsersRepository

import scala.slick.driver.MySQLDriver.simple._

object UsersRepositoryForTest extends UsersRepository {

	override val db = Database.forURL(
		url = s"jdbc:mysql://localhost:3306/testporrify",
		driver = "com.mysql.jdbc.Driver",
		user = "root",
		password = ""
	)

	override def tableName: String = "TEST_USERS"

	def removeAllRows() {
		db.withSession {
			implicit session =>
				usersRepository.delete
		}
	}
}