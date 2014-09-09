package basic

import com.typesafe.config.ConfigFactory
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
import com.readr.client.projects
import com.readr.client.meaning.frames
import com.readr.model.frame.Frame

object Example1CreateProject {
  
  def main(args:Array[String]):Unit = {
    val conf = ConfigFactory.load
    val host = conf.getString("HOST")
    val user = conf.getString("USER")
    val password = conf.getString("PASSWORD")
    val ns = conf.getString("NS")
    val proj = conf.getString("EXAMPLE_PROJ")
    
    //System.getProperty("user.dir")
    //val dir = conf.getString("DIR")

    Client.baseUrl = host + "/api"
    Client.user = user
    Client.password = password
    
    implicit val p = Project(ns, proj)
        
    Client.open
    
    if (projects.exists(p))
      println("Project already exists.")
    else
      projects.create(p)
    
    Client.close    
  } 
}