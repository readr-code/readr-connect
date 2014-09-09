[![Build Status](https://api.shippable.com/projects/53f2e991c4f33e48022b5360/badge/master)](https://www.shippable.com/projects/53f2e991c4f33e48022b5360)

# readr-connect

Examples showing how to use the Readr REST API to connect with Readr in the cloud. This makes it possible to push large document collections into Readr cloud, which makes it easy to explore the collections and create annotations as well as extraction patterns. Annotations and patterns can later be downloaded using the REST API.

readr-connect has been tested on MacOS X with Scala 2.10.4 and sbt 0.13.

To run the examples, you must first set the user, password, and ns fields in [conf/application.conf](conf/application.conf). The ns field (namespace) is automatically generated when you create an account on readr.com.

## 2. Basic API examples

Start by creating a project

`sbt "runMain example.CreateProject1"`  [source](src/main/scala/example/CreateProject1.scala)




[CreateProject2](src/main/scala/example/CreateProject2.scala)

[AddDocument1](src/main/scala/example/AddDocument1.scala)
[AddDocument2](src/main/scala/example/AddDocument2.scala)


See the examples in `src/main/scala/allenai/example` to see how to push patterns, fetch results, etc. You can run them as follows:

`sbt "runMain allenai.example.Example3CreateFrameWithPattern"`

`sbt "runMain allenai.example.Example4FetchPatternMatches"`

`sbt "runMain allenai.example.Example5FetchPatternAnnotations"`

Copying annotations only for given frame as text (easy to read and edit)

`sbt "runMain allenai.example.Example6FetchPatternAnnotations"`

`sbt "runMain allenai.example.Example7PutPatternAnnotations"`

Copying frames/rules/annotation for all frames using json (editable, but not very easily)

`sbt "runMain allenai.example.Example8FetchAllMeaning"`

`sbt "runMain allenai.example.Example9PutAllMeaning"`


## 3. Working with large corpora

For large corpora, we recommend to do all preprocessing locally (or on another cluster) and then push the results to Readr Cloud for exploration and pattern development.

Spark 1.0.1.

You must have Apache spark installed in a directory if you would like to process and push new datasets to readr. Fetch spark at `https://spark.apache.org/downloads.html`.



1. Convert sources into Readr format. 

`large.Example1CreateSource`

2. Run processing on Apache Spark.

Refer to readr-spark on more information on how this is done.

3. Push the results to Readr Cloud.

`large.Example2CreateDB`

You can now use Readr Cloud to create frames and patterns. When you are done using Readr Cloud you can fetch the rules and annotations you have created to store them locally. You can also write these back to Readr Cloud (to the same or a different Readr project). There are a bunch of different options:

large.Example3FetchFrames

large.Example4PutFrames

## 4. Copying rule sets

Readr Cloud makes it easy to create, manipulate, and test extraction rules. When you are done using Readr Cloud you can fetch the rules and annotations you have created to store them locally. You can also write these back to Readr Cloud (to the same or a different Readr project). There are a bunch of different options:

### Copying frames/rules using kryo (fast, but not human-readable)

`./fetch_frames.sh`

`./push_frames.sh`
 
### Copying annotations only for given frame as text (easy to read and edit)
    
`sbt "runMain allenai.example.Example6FetchPatternAnnotations"`

`sbt "runMain allenai.example.Example7PutPatternAnnotations"`
    
### Copying frames/rules/annotation for all frames using json (editable, but not very easily)

`sbt "runMain allenai.example.Example8FetchAllMeaning"`

`sbt "runMain allenai.example.Example9PutAllMeaning"`

