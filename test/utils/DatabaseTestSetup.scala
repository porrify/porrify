package utils

import scala.slick.driver.MySQLDriver.simple._

trait DatabaseTestSetup {

	private val databaseName = "testporrify"
	private val baseUrl = "jdbc:mysql://localhost"
	private val url = s"$baseUrl/$databaseName"
	private val driver = "com.mysql.jdbc.Driver"
	private val user = "root"
	private val password = ""

	def testDb(url: String = url) = Database.forURL(
		url = url,
		driver = driver,
		user = user,
		password = password
	)

	def createTestDatabase(): Unit = {
		import scala.slick.jdbc.{StaticQuery => Q}
		{
			testDb(baseUrl) withSession {
				implicit session =>
					Q.updateNA(s"CREATE DATABASE IF NOT EXISTS `$databaseName`").execute
			}
		}
	}

}
