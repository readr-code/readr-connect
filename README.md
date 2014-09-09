# readr-allenai

A set of examples that show how to use the Readr API to connect with Readr Cloud.
Also, scripts to download and process allenai's datasets.

## Prerequisites

The following has been tested on MacOS X with Scala 2.10.4, sbt 0.13, Spark 1.0.1.

You must have Apache spark installed in a directory if you would like to process and push new datasets to readr. Fetch spark at `https://spark.apache.org/downloads.html`.

If you would like to run Kevin's preprocessing scripts for the wikipedia corpora, you must also install xml, gsed. 

## 1. Setting Readr credentials

In `conf/application.conf` set the user and password fields.

## 2. Simple API examples

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

