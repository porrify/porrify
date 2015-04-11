package utils

import org.scalatest.BeforeAndAfterAll

trait DatabaseTestSetup extends BeforeAndAfterAll {

	self: org.scalatest.Suite =>

	def databaseName: String

	override protected def beforeAll(): Unit = {
		createDatabase()
	}

	private def createDatabase(): Unit = {
		import scala.slick.driver.MySQLDriver.simple._
		import scala.slick.jdbc.{StaticQuery => Q}
		{
			Database.forURL("jdbc:mysql://localhost/", driver = "com.mysql.jdbc.Driver", user = "root", password = "") withSession {
				implicit session =>
					Q.updateNA(s"CREATE DATABASE IF NOT EXISTS `$databaseName`").execute
			}
		}
	}
}
