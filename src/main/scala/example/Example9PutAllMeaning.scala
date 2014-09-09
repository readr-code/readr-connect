package example

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
import com.readr.client.meaning.frameArgs
import com.readr.client.meaning.frameValences
import com.readr.model.annotation.AnnotationConfirmationType
import com.readr.model.annotation.AnnotatedSentence
import com.readr.model.annotation.FrameMatchFeature
import java.util.regex.Pattern
import java.util.regex.Matcher
import java.io.File
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.FileInputStream
import com.readr.client.document.layerDefaults
import com.readr.client.document.frameMatchFeatures
import com.readr.model.FrameMatchFeatureLayerRef
import org.json4s.ShortTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write
import org.json4s.jackson.Serialization.read
import org.json4s.ShortTypeHints
import org.json4s.NoTypeHints
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._


object Example9PutAllMeaning extends Settings {
  implicit val formats = Serialization.formats(NoTypeHints)
  
  def main(args:Array[String]) = {
    implicit val p = Project(ns, proj)

    Client.open(host, user, password)

    val layerID = layerDefaults("FrameMatchFeature", "Manual")    
    implicit val lay = FrameMatchFeatureLayerRef(layerID)
    
    // clear
    frames.delete
    frameMatchFeatures.delete
        
    val outDir = tmpDir + "/test"
      
    for (f <- new File(outDir).listFiles) {
      val r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"))
      val frameID = r.readLine.toInt
      val frame = read[Frame](r.readLine)
      var newFrameID = frames.create(frame)
      
      for (arg <- frame.args)
        frameArgs.add(newFrameID, arg)

      frameValences.update(newFrameID, frame.valences(0).text)

      var l:String = null
      while ({ l = r.readLine; l != null }) {
        val fmf = read[FrameMatchFeature](l)
        frameMatchFeatures.add(fmf.copy(frameID = newFrameID))
      }      
      r.close
    }
      
    Client.close     
  } 
}