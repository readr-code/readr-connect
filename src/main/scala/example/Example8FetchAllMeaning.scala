package example

//import scala.collection.mutable._
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



object Example8FetchAllMeaning {
  implicit val formats = Serialization.formats(NoTypeHints)
  
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

    Client.open(host, user, password)

    val layerID = layerDefaults("FrameMatchFeature", "Manual")    
    implicit val lay = FrameMatchFeatureLayerRef(layerID)
        
    val outDir = "/tmp/test"
    
    // get frames
    val l:Seq[(Int,Frame)] = frames.listDetails
    
    // create document for each frame; get annotations
    for (t <- l) { t match {
      case (frameID,x) => 
        val w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outDir + "/" + x.name + ".json")))
        val json = write[Frame](x)
        w.write(frameID + "\n")
        w.write(json.toString + "\n")
        
        var offset = 0
        var hasMore = true
        while (hasMore) {
          val ls = frameMatchFeatures.findByFrameID(frameID, offset, 1000)
          for (r <- ls) {
            val jsonm = write[FrameMatchFeature](r)
            w.write(jsonm + "\n")
          }
          if (ls.size < 1000)
            hasMore = false
          else
            offset += 1000
        }
        w.close()
      }
    }
    
    Client.close     
  } 
}