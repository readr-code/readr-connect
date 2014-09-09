package example

import scala.collection.mutable.Seq

import com.readr.client.Client
import com.readr.client.meaning.frameArgs
import com.readr.client.meaning.frameValences
import com.readr.client.meaning.frames
import com.readr.model.Project
import com.readr.model.frame.Frame
import com.readr.model.frame.FrameArg
import com.readr.model.frame.FrameType
import com.typesafe.config.ConfigFactory

object Example3CreateFrameWithPattern extends Settings {
  
  def main(args:Array[String]) = {
    implicit val p = Project(ns, proj)
        
    Client.open(host, user, password)
    
    val f = Frame(
      name = "1sttest", 
      description = "",
      examples = 
        """|In the hot weather our bodies sweat perspiration bringing water to our[our] skin.
           |("our bodies"/?x "sweat" "perspiration" [ "In the hot weather" ] )	""/EFFECT-55	("our bodies"/?x "bring" "water" [ "to our[our] skin" ] )""".stripMargin, 
	    typ = FrameType.Verb // currently not used
    )

    //val frameID = frames.create(f)
    val rv = frames.addMany(Seq(f))
    val frameID = rv(0)
    frameArgs.add(frameID, FrameArg(frameID, 0, "r", "root", true))
    frameArgs.add(frameID, FrameArg(frameID, 1, "c", "complement", true))
    frameValences.update(frameID, 
      """|dep(r, "xcomp", c) & partOfSpeech(c, "VBG") & ~(exists d dep(c, "ccomp", d))
         |
         |dep(d, "compmod", r)&dep(d, "det", c)&token(d,'.')
         |dep(a, "xcomp", b)""".stripMargin)
    
    Client.close    
  } 
}