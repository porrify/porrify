import play.api.{Application, GlobalSettings}
import repository.UsersRepository

object Global extends GlobalSettings {
	override def beforeStart(app: Application): Unit = {
		import scala.slick.driver.MySQLDriver.simple._
		import scala.slick.jdbc.{StaticQuery => Q}
		{
			Database.forURL("jdbc:mysql://localhost/", driver = "com.mysql.jdbc.Driver", user = "root", password = "") withSession {
				implicit session =>
					Q.updateNA(s"CREATE DATABASE IF NOT EXISTS `porrify`").execute
			}
		}
	}

	override def onStart(app: Application): Unit = {
		UsersRepository.createTable()
	}
}
