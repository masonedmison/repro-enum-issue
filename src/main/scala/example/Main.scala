package example

import cats.effect.{IO, IOApp}
import doobie.Transactor
import doobie.implicits.{toSqlInterpolator, toConnectionIOOps}
import doobie._
import doobie.postgres.implicits.pgEnumStringOpt
import example.Main.Mood.happy
import example.Main.Mood.sad
import example.Main.Mood.ok

object Main extends IOApp.Simple {
  sealed trait Mood {
    val entryName: String = this match {
      case _: happy.type => "happy"
      case _: sad.type => "sad"
      case _: ok.type => "ok"
    }
  }
  object Mood {
    case object happy extends Mood
    case object sad extends Mood
    case object ok extends Mood
    def toEnum(s: String): Option[Mood] = s match {
      case "happy" => Some(Mood.happy)
      case "ok" => Some(Mood.ok)
      case "sad" => Some(Mood.sad)
      case _ => None
    }

    implicit val forMeta: Meta[Mood] =
      pgEnumStringOpt("mood", toEnum, _.entryName)

  }
  def selectOkPeople(person: String = "Bob") = {
    val query = 
    Fragment.const("SELECT name FROM Person") ++
      Fragments.whereAnd(
        fr"mood = ${Mood.ok}"
        //fr"name = $person"
      )

    println(s"####Query Fragment:\n$query")

    query
      .query[String]
      .to[List]
  }

  override def run: IO[Unit] = {
    val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
      driver ="org.postgresql.Driver", // driver classname
      url = "jdbc:postgresql:repro", // connect URL (driver-specific)
      user = "postgres", // user
      password = "postgres", // password
      logHandler = None
    )

    selectOkPeople().transact(xa)
      .flatTap { results => IO.println(results.mkString)}
      .void
  }
}
