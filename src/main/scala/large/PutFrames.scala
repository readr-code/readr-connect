package large

import scala.collection.mutable._
import scala.io.Source
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io._
import com.readr.model.annotation.TextAnn
import com.readr.model.annotation.TextFragment
import com.readr.model.annotation.TextFragmentAnn
import com.readr.model.Offsets
import com.readr.model.annotation.Annotations
import com.readr.client.util.AnnotationSequenceFileReader
import com.readr.client.Client
import com.readr.model.Project
import com.readr.client.meaning.frames
import com.readr.model.frame.Frame


object PutFrames {
  
  private def usage =
    println("PutFrames HOST USER PASSWORD NS PROJ DIR")
    
  def main(args:Array[String]) = {
    val host = args(0)        // http://preview.readr.com:9000
    val user = args(1)        // a
    val password = args(2)    // a
    val ns = args(3)          // allenai
    val proj = args(4)        // barrons-4th-grade
    val dir = args(5)         // /Users/.../data
    
    Client.baseUrl = host + "/api"
    Client.user = user
    Client.password = password
    
    implicit val p = Project(ns, proj)
        
    val conf = new Configuration()
    val sf = new AnnotationSequenceFileReader(conf, Array(classOf[Frame]), dir + "/data.colX.Frame")

    for (clazz <- Annotations.annWithDependentClazzes)
      sf.register(clazz)
    
    val fs = new ArrayBuffer[(Int,Frame)]()
    var n:(Long,Array[Any]) = null
    while ({ n = sf.read; n != null} ) {
      val i = n._1
      val f = n._2
      fs += Tuple2(i.toInt, f(0).asInstanceOf[Frame])
    }
    sf.close
      
    Client.open
    
    frames.addMany(fs.map(x => x._2))
    
    Client.close
    
  } 
}