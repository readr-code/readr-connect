package example

import example.FetchPatternAnnotations2._

import scala.collection.mutable._
import scala.io.Source
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io._
import com.readr.model.annotation._
import com.readr.model.Offsets
import com.readr.model.annotation.Annotations
import com.readr.client.util.AnnotationSequenceFileReader
import com.readr.client.Client
import com.readr.model.Project
import com.readr.client.meaning.frames
import com.readr.model.frame.Frame
import com.typesafe.config.ConfigFactory
import com.readr.client.meaning.frames
import com.readr.client.meaning.frameValences
import com.readr.model.annotation.AnnotationConfirmationType
import com.readr.model.annotation.AnnotatedSentence
import com.readr.model.annotation.FrameMatchFeature
import java.util.regex.Pattern
import java.util.regex.Matcher
import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.FileInputStream
import com.readr.client.document.layerDefaults
import com.readr.client.document.frameMatchFeatures
import com.readr.model.FrameMatchFeatureLayerRef

object PutPatternAnnotations extends Settings {
  
  def main(args:Array[String]) = {
    implicit val p = Project(ns, proj)

    Client.open(host, user, password)

    val layerID = layerDefaults("FrameMatchFeature", "Manual")    
    implicit val lay = FrameMatchFeatureLayerRef(layerID)        
    
    val frameID = frames.idByName("frame3")
    if (frameID == -1)
      println("Frame not found")

    val dir = tmpDir + "/test"
    val f = new File(dir + "/annotations")
    val reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"))
    val docPattern = Pattern.compile("doc (.*), sentNum (.*), sentenceTokenOffset (.*), truth (.*)")
    val argPattern = Pattern.compile("arg (.*): (.*)")
    
    var documentID = -1
    var sentNum = -1
    var sentenceTokenOffset = -1
    var truth = false
    var args = ArrayBuffer[FrameMatchFeatureArg]()
    var l:String = null
    while ({ l = reader.readLine(); l != null} ) {
      if (l.startsWith("//")) {
        // ignore sentence
        if (documentID != -1) {
          val fmf = FrameMatchFeature(
            instanceID = -1,
            frameID,
            truth = truth,
            priority = 0,
            args = args
          )
          frameMatchFeatures.add(fmf)
          args.clear
        }
        
      } else if (l.startsWith("doc ")) {
    	val m = docPattern.matcher(l)
    	m.find
    	//println(l)
    	documentID = m.group(1).toInt
    	sentNum = m.group(2).toInt
    	sentenceTokenOffset = m.group(3).toInt
    	truth = m.group(4).toBoolean
              
      } else if (l.startsWith("arg ")) {
        val m = argPattern.matcher(l)
        m.find
        val argNum = m.group(1).toByte
        val pos = m.group(2).toInt
        args += FrameMatchFeatureArg(argNum, documentID, sentenceTokenOffset + pos)
      }
    }
    val fmf = FrameMatchFeature(
      instanceID = -1,
      frameID,
      truth = truth,
      priority = 0,
      args = args
    )
    frameMatchFeatures.add(fmf)
    
    reader.close
    
    Client.close     
  } 
}