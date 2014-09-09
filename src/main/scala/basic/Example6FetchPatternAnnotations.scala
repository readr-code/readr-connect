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
import com.readr.model.annotation.FrameMatchFeature
import java.io.File
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FileOutputStream

object Example6FetchPatternAnnotations {
  
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
        
    // store annotations in local file
    val outDir = "/tmp/test"
    if (new File(outDir).exists) {
      println("/tmp/test already exists, you must delete that first")
      System.exit(1)
    }
    new File(outDir).mkdirs
    
    val f = new File(outDir + "/annotations")
    
    Client.open        
    
    val frameID = frames.idByName("frame2")
    if (frameID == -1)
      println("Frame not found")

    val frameCounts = frameValences.findMatches(frameID)
    println("totalMatches = " + frameCounts.totalMatches)
    println("confirmedMatches = " + frameCounts.confirmedMatches)
    println("...")
    
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"))

    var offset = 0
    var hasMore = true
    
    while (hasMore) {
    
      val prs = frameValences.sentences(AnnotationConfirmationType.AllAnnotations, frameID, offset, 1000)
    
      if (prs.list.isEmpty)
        println("No annotations.")
      
      for (as:AnnotatedSentence <- prs.list) {
        val ann = as.annotations
        val textAnn = ann(0).asInstanceOf[TextAnn]
        val tokenOffsetAnn = ann(1).asInstanceOf[TokenOffsetAnn]
        val sentenceOffsetAnn = ann(2).asInstanceOf[SentenceOffsetAnn]
        val sentenceTokenOffsetAnn = ann(3).asInstanceOf[SentenceTokenOffsetAnn]        
        val framePatterns = ann(4).asInstanceOf[FrameMatchFeatureAnn]
        val frameManual = ann(5).asInstanceOf[FrameMatchFeatureAnn]
      
        val sto = sentenceTokenOffsetAnn.sents(0).f
      
        writer.write("// " + textAnn.text + "\n")
        writer.write("doc " + as.documentID + ", sentNum " + as.sentNum + ", sentenceTokenOffset " + sto + ", truth " + frameManual.features(0).truth + "\n")
        for (arg <- frameManual.features(0).args) 
          writer.write("arg " + arg.argNum + ": " + (arg.pos - sto) + "\n")
      }
      
      hasMore = prs.hasMore
      offset = offset + prs.list.size
    }

    writer.close
    
    Client.close 
  } 
}