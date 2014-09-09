package large

import com.readr.client._
import com.readr.model.Project
import com.readr.client.document.layers
import com.readr.client.document.layerDefaults
import com.readr.model.layer._
import com.readr.client.system.management
import com.readr.model.ProjectDetails

object CreateDB {

  private def usage =
    println("CreateDB HOST USER PASSWORD NS PROJ DIR")
    
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
    
    Client.open
    
    projects.delete
    projects.create(p)
    projects.update(ProjectDetails(ns, proj, "", "public"))

    val l0 = layers.create(Layer("text", "Text"))
    val l1 = layers.create(Layer("toks", "Token"))
    val l2 = layers.create(Layer("sents", "Sentence"))
    val l3 = layers.create(Layer("deps", "Dependency"))
    val l4 = layers.create(Layer("ner", "NER"))
    val l5 = layers.create(Layer("frames", "FrameMatchFeature"))
    val l6 = layers.create(Layer("framesManual", "FrameMatchFeature"))
    val l7 = layers.create(Layer("pos", "POS"))
    val l8 = layers.create(Layer("lemma", "Lemma"))
    
    layerDefaults.set("Text", "", l0)
    layerDefaults.set("Token", "", l1)
    layerDefaults.set("Sentence", "", l2)
    layerDefaults.set("Dependency", "", l3)
    layerDefaults.set("NER", "", l4)
    layerDefaults.set("FrameMatchFeature", "", l5)
    layerDefaults.set("FrameMatchFeature", "Manual", l6)
    layerDefaults.set("POS", "", l7)
    layerDefaults.set("Lemma", "", l8)
   
    importIndices(ns, proj, dir)
    
    Client.close        
  }
  
  def importIndices(ns:String, proj:String, dir:String) = {
    val textLayerID = 1
    val tokenLayerID = 2
    val sentenceLayerID = 3
    val dependencyLayerID = 4
    val nerLayerID = 5
    val posLayerID = 8
    val lemmaLayerID = 9

        // import documents
    management.uploadAndImportTableFromDir(ns, proj,
        "document",
        dir + "/document")

    // import texts
    management.uploadAndImportTableFromDir(ns, proj,
        "text" + textLayerID,
        dir + "/text"
    )
    
    // import tokens
    management.uploadAndImportTableFromDir(ns, proj,
        "token2name" + tokenLayerID,
        dir + "/token2name")
    
    management.uploadAndImportTableFromDir(ns, proj,
        "tokenInst2basic" + tokenLayerID,
        dir + "/tokenInst2basic")

    management.uploadAndImportTableFromDir(ns, proj,
        "documentTokenOffsets" + tokenLayerID,
        dir + "/documentTokenOffsets")
        
    // import sentences
    management.uploadAndImportTableFromDir(ns, proj,
        "sentence" + sentenceLayerID,
        dir + "/sentence")
        
    management.uploadAndImportTableFromDir(ns, proj,
        "sentenceTextToken" + sentenceLayerID,
        dir + "/sentenceTextToken")
        
    // import dependencies
    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "dependency2name" + dependencyLayerID, 
        /* dataFile */  dir + "/dependency2name")
    
    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "dependencyInst2basic" + dependencyLayerID, 
        /* dataFile */  dir + "/dependencyInst2basic")
        
    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "sentenceDependency" + dependencyLayerID, 
        /* dataFile */  dir + "/sentenceDependency")

    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "partOfSpeechInst" + posLayerID, 
        /* dataFile */  dir + "/partOfSpeechInst")

    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "partOfSpeech" + posLayerID, 
        /* dataFile */  dir + "/partOfSpeech")
        
    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "lemmaInst" + lemmaLayerID, 
        /* dataFile */  dir + "/lemmaInst")

    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "lemma" + lemmaLayerID, 
        /* dataFile */  dir + "/lemma")
        
    // import NER
    management.uploadAndImportTableFromDir(ns, proj, 
        /* tableName */ "ner" + nerLayerID, 
        /* dataFile */  dir + "/ner")
//
//    // import distsim
//    management.importTableFromDir(ns, proj,
//        /* tableName */ "distsim",
//        /* dataFile */ dir + "/distsim")
//        
//    // import patternBinary
//    management.importTableFromDir(ns, proj,
//        /* tableName */ "posPairFeature0",
//        /* dataFile */ dir + "/posPairFeature")
//        
//    management.importTableFromDir(ns, proj,
//        /* tableName */ "posPairFeatureCount0",
//        /* dataFile */ dir + "/posPairFeatureCount")
//        
//    management.importTableFromDir(ns, proj,
//        /* tableName */ "posPairFeatureInst0",
//        /* dataFile */ dir + "/posPairFeatureInst")
//        
//    // import mentions
//    management.importTableFromDir(ns, proj,
//        /* tableName */ "mention0",
//        /* dataFile */ dir + "/mention")
//        
//    management.importTableFromDir(ns, proj,
//        /* tableName */ "corefCluster0",
//        /* dataFile */ dir + "/corefCluster")
//
////    management.importTableFromDir(ns, proj,
////        /* tableName */ "mention2cluster0",
////        /* dataFile */ dir + "/mention2cluster")
//
//    management.importTableFromDir(ns, proj,
//        /* tableName */ "distantCluster0",
//        /* dataFile */ dir + "/distantCluster")
//    

  }
}