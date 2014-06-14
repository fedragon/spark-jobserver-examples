package sparking
package jobserver

import com.typesafe.config.Config
import org.apache.spark.SparkContext, SparkContext._
import org.apache.spark.rdd.RDD

trait UsersSparkJob extends spark.jobserver.SparkJob with spark.jobserver.NamedRddSupport with UsersRDDBuilder {
  val rddName = "users"

  // Validation is not really needed in this example
  def validate(sc: SparkContext, config: Config): spark.jobserver.SparkJobValidation = spark.jobserver.SparkJobValid
}

object GetOrCreateUsers extends UsersSparkJob {

  override def runJob(sc: SparkContext, config: Config) = {
    val users: RDD[(Reputation, User)] = namedRdds.getOrElseCreate(
      rddName,
      build(sc))

    users.take(5)
  }
}

object GetOrFailUsers extends UsersSparkJob {

  override def runJob(sc: SparkContext, config: Config) = {
    val users: Option[RDD[(Reputation, User)]] = namedRdds.get(rddName)

    users.map(_.take(5)).getOrElse(throw new IllegalStateException(s"RDD [$rddName] does not exist!"))
  }
}

object GetAndUpdateUsers extends UsersSparkJob {

  override def runJob(sc: SparkContext, config: Config) = {
    val users: Option[RDD[(Reputation, User)]] = namedRdds.get(rddName)

    users.map { rdd =>
      val newcomers = namedRdds.update(
        rddName,
        rdd.filter {
          case (_, user) => user.activityDays < 100
        })

      newcomers.take(5)
    }.getOrElse(throw new IllegalStateException(s"RDD [$rddName] does not exist"))
  }
}

