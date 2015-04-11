package utils

import repository.UsersRepository

import scala.slick.driver.MySQLDriver.simple._

object UsersRepositoryForTest extends UsersRepository with DatabaseTestSetup {

	override val db = testDb()

	override def tableName: String = "TEST_USERS"

	def removeAllRows() {
		db.withSession {
			implicit session =>
				usersRepository.delete
		}
	}
}