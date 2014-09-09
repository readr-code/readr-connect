package example

import com.readr.client.{Client, projects}
import com.readr.model.Project

// Create a new project with a set of annotation layers and a set default
// layer for each annotation type
object CreateProject1 extends Settings {
  
  def main(args:Array[String]):Unit = {

    Client.open(host, user, password)

    implicit val p = Project(ns, proj)

    if (projects.exists(p))
      println("Project already exists.")
    else
      projects.createWithDefaults
    
    Client.close    

  }
}