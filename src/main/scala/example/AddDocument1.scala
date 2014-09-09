package example

import com.readr.client.{interactive, Client}
import com.readr.client.document.{documents, layerDefaults, texts}
import com.readr.model.annotation.TextAnn
import com.readr.model.{Project, TextLayerRef, TokenLayerRef}

object AddDocument1 extends Settings {
  
  def main(args:Array[String]):Unit = {

    Client.open(host, user, password)

    implicit val p = Project(ns, proj)

    val text = "This is a very simple document."

    // manually compute annotations and store

    val documentID = documents.createWithLayers

    interactive.runPlan(documentID, text)

    Client.close
  } 
}