[![Build Status](https://api.shippable.com/projects/53f2e991c4f33e48022b5360/badge/master)](https://www.shippable.com/projects/53f2e991c4f33e48022b5360)

# readr-connect

Examples showing how to use the Readr REST API to connect with Readr in the cloud. This makes it possible to push large document collections into Readr cloud, which makes it easy to explore the collections and create annotations as well as extraction patterns. Annotations and patterns can later be downloaded using the REST API.

readr-connect has been tested on MacOS X with Scala 2.10.4 and sbt 0.13.

To run the examples, you must first set the user, password, and ns fields in [conf/application.conf](conf/application.conf). The ns field (namespace) is automatically generated when you create an account on readr.com.

## 1. Basic API examples

Start by creating a project

`sbt "runMain example.CreateProject1"`    [source](src/main/scala/example/CreateProject1.scala)

and adding a document

`sbt "runMain example.AddDocument1"`    [source](src/main/scala/example/AddDocument1.scala)

These two examples actually do slightly more than that: the first creates a project with a set of default
annotation layers, and the second precomputes these annotations when storing the document. The annotations
include tokenization, lemmatization, and more. For more fine grained control over annotations see 
[source](src/main/scala/example/CreateProject2.scala) and [source](src/main/scala/example/AddDocument2.scala).

Next, we will create a semantic frame with an extraction pattern

`sbt "runMain example.CreateFrameWithPattern"`    [source](src/main/scala/example/CreateFrameWithPattern.scala)

and fetch the matches for our extraction pattern

`sbt "runMain example.FetchPatternMatches"`    [source](src/main/scala/example/FetchPatternMatches.scala)

At this point, you can also validate a few of the generated matches using the web interface, or create additional annotations. These can then be retrieved using

`sbt "runMain example.FetchPatternAnnotations1"`    [source](src/main/scala/example/FetchPatternAnnotations.scala1)

The previous example writes these annotations to the screen, but of course we can also store these in a file ([source](src/main/scala/example/FetchPatternAnnotations2.scala)), and later push them back into the cloud ([source](src/main/scala/example/PutPatternAnnotations.scala)). While these examples handle the case for one given frame, we can also fetch and write back all frames, patterns, and annotations, at once, as shown in examples [source](src/main/scala/example/FetchAllMeaning.scala) and [source](src/main/scala/example/PutAllMeaning.scala).

## 2. Working with large corpora

For large corpora, we recommend doing all preprocessing locally (or on another cluster) and then push the results to Readr cloud. For processing we use Apache Spark. You must have Apache spark installed in a directory if you would like to process and push new datasets to readr. Fetch spark at `https://spark.apache.org/downloads.html`. We have tested our system on Spark 1.0.2.

Start by converting your documents into the readr format.

`sbt "runMain example.large.CreateSource"`    [source](src/main/scala/example/large/CreateSource.scala)

Then, run your processing on Apache Spark. The [readr-spark](http://github.com/readr-code/readr-spark) project contains more information on how this is done.

Finally, you can push the results to Readr cloud.

`sbt "runMain example.large.CreateDB"`    [source](src/main/scala/example/large/CreateDB.scala)

Spark uses Kryo for efficient serialization and deserialization of objects. We can also fetch and write back frames in Kryo. This makes it easy to generate a large number of frames, for example based on a resource. 

`sbt "runMain example.large.FetchFrames"`    [source](src/main/scala/example/large/FetchFrames.scala)

`sbt "runMain example.large.PutFrames"`    [source](src/main/scala/example/large/PutFrames.scala)
