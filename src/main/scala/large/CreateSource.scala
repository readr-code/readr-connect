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
import com.readr.client.util.AnnotationSequenceFileWriter

object CreateSource {
  
  //val headersFile   = Settings.srcDir + "/campbell-biology/book-headers.txt"
  //val sentencesFile = Settings.srcDir + "/campbell-biology/book-sentences.txt"
  //val outDir        = Settings.outDir + "/campbell-biology"
  
  def main(args:Array[String]) = {
    val headersFile = args(0)
    val headersCharset = args(1)
    val sentencesFile = args(2)
    val sentencesCharset = args(3)
    val outDir = args(4)
    
    val conf = new Configuration()

    val sfText = new AnnotationSequenceFileWriter(conf, outDir + "/data.col0.TextAnn")
    val sfTextFragment = new AnnotationSequenceFileWriter(conf, outDir + "/data.col1.TextFragmentAnn")
    val sfSource = new AnnotationSequenceFileWriter(conf, outDir + "/data.col2.Source")

    for (clazz <- Annotations.annWithDependentClazzes) {
      sfText.register(clazz)
      sfTextFragment.register(clazz)
      sfSource.register(clazz)
    }
    
    val headers = new HashMap[String,String]()
    
    for (line <- Source.fromFile(headersFile, headersCharset).getLines()) {
      val t = line.split("\t")
      if (t.length < 2)
        headers += t(0) -> ""
      else
        headers += t(0) -> t(1)
    }
    
    val fragments = ArrayBuffer[TextFragment]()
    var fraStart = 0
    var curSec:String = null
    var curPar:String = null
    val sb = new StringBuilder
    var id = 0

    for (line <- Source.fromFile(sentencesFile, sentencesCharset).getLines()) {
      val t = line.split("\t")
      val label = t(0)
      val sentence = if (t.length < 2) "" else t(1)
      //val sentence = t(1)
      val li = label.lastIndexOf(".")
      val lj = label.substring(0, li).lastIndexOf(".")
      val sec = label.substring(0, lj)
      val par = label.substring(lj+1, li)
      
	  if (curSec == null || !curSec.equals(sec)) {
		if (curSec != null) {
		  // write section as document to db
		  var text = sb.toString
		  fragments += TextFragment("par", Offsets(fraStart, sb.length), true)
		  text += "\n"

		  val ta = TextAnn(text)
		  val tfa = TextFragmentAnn(fragments.toArray)
		  val so = com.readr.model.annotation.Source("barrons", "", "")
		  
		  sfText.write(id, ta)
		  sfTextFragment.write(id, tfa)
		  sfSource.write(id, so)
		  
		  id += 1
	    }
	    // now create new document
	    sb.setLength(0)
	    fragments.clear
	    
	    val header = headers.getOrElse(sec, "")
	    sb.append(header)
	    //sb.append(headers.get(sec).get)
	    fragments += TextFragment("title", Offsets(0, sb.length), true)
	    sb.append("\n\n")
	    fraStart = sb.length
	    curPar = null
	    curSec = sec
	  }
      
      if (curPar == null || !curPar.equals(par)) {
		if (curPar != null) {
		  fragments += TextFragment("par", Offsets(fraStart, sb.length), true)		  
		  sb.append("\n\n")
		  fraStart = sb.length
		}
		curPar = par
	  }
			
	  sb.append(sentence)
	  sb.append(" ")
    }

    sfText.close
    sfTextFragment.close
    sfSource.close
  } 
}