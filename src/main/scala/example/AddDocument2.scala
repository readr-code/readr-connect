package example

import com.readr.client.{interactive, Client}
import com.readr.client.document.{tokens, documents, layerDefaults, texts} //, lemmas}
import com.readr.model.annotation.{TokensAnn, TokenOffsetAnn, TextAnn, LemmaAnn}
import com.readr.model.{LemmaLayerRef, Project, TextLayerRef, TokenLayerRef}

object AddDocument2 extends Settings {
  
  def main(args:Array[String]):Unit = {

    Client.open(host, user, password)

    implicit val p = Project(ns, proj)

    val text = "This is a very simple document."

    // manually compute annotations and store

    val layDef = layerDefaults.allDefaultsMap
    implicit val l1 = TextLayerRef(layDef("Text"))
    implicit val l2 = TokenLayerRef(layDef("Token"))
    //implicit val l3 = LemmaLayerRef(layDef("Lemma"))

    val documentID = documents.createWithLayers

    // compute some annotations
    val ta = TextAnn(text)

    val arr0 = interactive.run("com.readr.spark.stanford34.StanfordTokenizer", ta)
    val toa = arr0(0).asInstanceOf[TokenOffsetAnn]
    val to = arr0(1).asInstanceOf[TokensAnn]

//    val arr1 = interactive.run("com.readr.spark.stanford34.StanfordLemmatizer", ta, toa)
//    val la = arr0(0).asInstanceOf[LemmaAnn]

    texts.set(documentID, TextAnn(text))
    tokens.set(documentID, toa)(ta)
//    lemmas.set(documentID, la)

    Client.close
  } 
}