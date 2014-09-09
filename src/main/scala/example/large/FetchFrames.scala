package example.large

import example.Settings
import org.apache.hadoop.conf.Configuration
import com.readr.model.annotation.Annotations
import com.readr.model.Project
import com.readr.model.frame.Frame
import com.readr.client.Client
import com.readr.client.meaning.frames
import com.readr.client.util.AnnotationSequenceFileWriter

object FetchFrames extends Settings {
  
  def main(args:Array[String]) = {
    implicit val p = Project(ns, proj)
    
    Client.open(host, user, password)
    
    val fs:Seq[(Int,Frame)] = frames.listDetails
    
    Client.close
    
    val conf = new Configuration()
    val sf = new AnnotationSequenceFileWriter(conf, tmpDir + "/data.colX.Frame")

    for (clazz <- Annotations.annWithDependentClazzes)
      sf.register(clazz)
    
    for ((i,f) <- fs)
      sf.write(i, f)
    
    sf.close      
  } 
}