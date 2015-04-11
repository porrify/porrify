package model

case class User(id: Option[Int], username: String, email: String, password: String)

object User{

	def apply(data: (String, String, String, String)): User = {
		User(id = None, username = data._1, email = data._2, password = data._3)
	}

	def tupled(tuple: (Option[Int], String, String, String)): User = {
		User(tuple._1, tuple._2, tuple._3, tuple._4)
	}
}