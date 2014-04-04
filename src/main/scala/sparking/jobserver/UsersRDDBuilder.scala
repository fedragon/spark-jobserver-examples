package sparking
package jobserver

import org.apache.spark.SparkContext, SparkContext._
import org.apache.spark.rdd.RDD

trait UsersRDDBuilder {

  val inputPath = "/tmp/stackoverflow.com-Users"

  def build(sc: SparkContext): RDD[(Reputation, User)] = {
    sc.textFile(inputPath).
      map(User.fromRow).
      collect {
        case Some(user) => user.reputation -> user
      }.
      sortByKey(ascending = false)
  }

}
