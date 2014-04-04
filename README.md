# Spark Job Server Examples

Experiments with [Apache Spark](http://spark.apache.org) and recently-outsourced [Ooyala's Spark Job Server](https://github.com/ooyala/spark-jobserver).

## Reasons for Spark Job Server:

* Allows you to share Spark Contexts between jobs (!!);
* Provides a RESTful API to manage jobs, contexts and jars.

## Goal

Let's find out the Top 5 Stack Overflow users (by sheer reputation!).

In this example there are 3 implementations of `spark.jobserver.SparkJob`: their common goal is to get the top 5 users out of the `users` RDD but they have different behaviours:

* GetOrCreateUsers: tries to get the RDD **or creates it**, if it doesn't exist;
* GetOrFailUsers: tries to get the RDD **or throws an exception**, if it doesn't exist;
* GetAndUpdateUsers: tries to get the RDD **and updates it** to only include users which signed up in the last 100 days, then returns the top 5 users; throws an exception, if the RDD doesn't exist.

## Prerequisites

### Download StackOverflow's users file

Download `stackoverflow.com-Users.7z` from [Stack Exchange Data Dump](https://archive.org/details/stackexchange), uncompress it and put it in `/tmp`.

### Clone Ooyala's Spark Job Server

    $ git clone https://github.com/ooyala/spark-jobserver
    $ cd spark-jobserver

#### Publish it to your local repository and run it

    $ sbt publish-local
    $ sbt
    > re-start

This will start a background server process which will run until you close sbt.

### Clone this project and package it

    $ git clone https://github.com/fedragon/spark-jobserver-examples
    $ sbt package

## Get your hands dirty with Spark Jobserver

### Deploy our jar

    curl --data-binary @target/scala-2.10/spark-jobserver-examples_2.10-1.0.0.jar localhost:8090/jars/sparking

    curl 'localhost:8090/jars'

### Create the context that will be shared

    curl -X POST 'localhost:8090/contexts/users-context'

    curl 'localhost:8090/contexts'

### Run our jobs

#### 0. How to check job status/response:

To find a single job you can use:

    curl 'localhost:8090/jobs/<job-id>''

the actual `job-id` can be found in the response you get when you run the job (see below).

In alternative, you can find all the jobs using:

    curl 'localhost:8090/jobs'


#### 1. GetOrFailUsers

    curl -X POST 'localhost:8090/jobs?appName=sparking&classPath=sparking.jobserver.GetOrFailUsers&context=users-context'

Check the job status/response as described above: if you are following this README and you are running this job before any other, it will fail (as intended) because the `users` RDD does not exist yet.

#### 2. GetOrCreateUsers

    curl -X POST 'localhost:8090/jobs?appName=sparking&classPath=sparking.jobserver.GetOrCreateUsers&context=users-context'

Check the job status/response as described above: once the job completes, the response will contain the top 5 users.

**Note:** now that the `users` RDD has been created and cached, you can re-run GetOrFailUsers and see it complete successfully (and fast)!

#### 3. GetAndUpdateUsers

    curl -X POST 'localhost:8090/jobs?appName=sparking&classPath=sparking.jobserver.GetAndUpdateUsers&context=users-context'

Check the job status/response as described above: once the job completes, it will return the top 5 users among those who signed up in the last 100 days.

### Check jobs' completion times

    curl 'localhost:8090/jobs'

You should now see a big difference between the time it took the first job (= the one that actually created the `users` RDD) to complete and the other jobs' times.

## Where to go next

This example only shows a few features from Ooyala's Spark Job Server so I recommend you to go [here!](https://github.com/ooyala/spark-jobserver/blob/master/README.md)