package sparking
package jobserver

import org.joda.time.{Days, LocalDate}
import org.joda.time.format.DateTimeFormat

case class User(displayName: String, reputation: Int, activityDays: Int)

object User {

  // This is never going to be the best regex ever seen, but it's good enough for this example.
  val Regex = """^.*Reputation="([0-9]+)" CreationDate="([0-9]{4}-[0-9]{2}-[0-9]{2})T.*" DisplayName="(.+)" LastAccess.*$""".r
  val DateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd")

  def fromRow(row: String) = row match {
    case Regex(reputation, activityDays, displayName) => 
      val activity = Days.daysBetween(LocalDate.parse(activityDays, DateTimeFormatter), LocalDate.now)
      Some(User(displayName, reputation.toInt, activity.getDays))
    case _ => None
  }
}
