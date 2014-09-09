package basic

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

object Example4FetchPatternMatches {
  
  def main(args:Array[String]) = {
    val conf = ConfigFactory.load
    val host = conf.getString("HOST")
    val user = conf.getString("USER")
    val password = conf.getString("PASSWORD")
    val ns = conf.getString("NS")
    val proj = conf.getString("EXAMPLE_PROJ")
    
    Client.baseUrl = host + "/api"
    Client.user = user
    Client.password = password
    
    implicit val p = Project(ns, proj)
        
    Client.open
    
    val frameID = frames.idByName("1sttest")
    if (frameID == -1)
      println("Frame not found")
    
    val frameCounts = frameValences.findMatches(frameID)
    println("totalMatches = " + frameCounts.totalMatches)
    println("confirmedMatches = " + frameCounts.confirmedMatches)
    println("...")

    val prs = frameValences.sentences(AnnotationConfirmationType.AllMatches, frameID, 0, 100)
    if (prs.list.isEmpty)
      println("No matches.")
      
    for (as:AnnotatedSentence <- prs.list) {
      println("doc " + as.documentID + ", sentNum " + as.sentNum)
      
      val ann = as.annotations
      val textAnn = ann(0).asInstanceOf[TextAnn]
      val tokenOffsetAnn = ann(1).asInstanceOf[TokenOffsetAnn]
      val sentenceOffsetAnn = ann(2).asInstanceOf[SentenceOffsetAnn]
      val sentenceTokenOffsetAnn = ann(3).asInstanceOf[SentenceTokenOffsetAnn]        
      val framePatterns = ann(4).asInstanceOf[FrameMatchFeatureAnn]
      val frameManual = ann(5).asInstanceOf[FrameMatchFeatureAnn]
        
      println(textAnn.text)
      for (arg <- framePatterns.features(0).args) 
        println("arg " + arg.argNum + ": " + (arg.pos - sentenceTokenOffsetAnn.sents(0).f))
    }
    
    if (prs.hasMore)
      println("Note: There are more matches.")
    
    Client.close 
  }
}