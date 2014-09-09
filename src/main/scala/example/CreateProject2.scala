package example

import com.readr.client.document.{layerDefaults, layers}
import com.readr.client.{Client, projects}
import com.readr.model.Project
import com.readr.model.layer.Layer

// Create an empty project without annotation layers,
// and then manually create a few annotation layers
// and set defaults for different annotation types.
object CreateProject2 extends Settings {
  
  def main(args:Array[String]):Unit = {

    Client.open(host, user, password)

    implicit val p = Project(ns, proj)

    if (projects.exists(p))
      println("Project already exists.")
    else {
      projects.create(p)

      val l1 = Layer("text", "Text")
      val l2 = Layer("tokens", "Token")

      val id1 = layers.create(l1)
      val id2 = layers.create(l2)

      layerDefaults.set("Text", "", id1)
      layerDefaults.set("Token", "", id2)
    }
    Client.close

  } 
}