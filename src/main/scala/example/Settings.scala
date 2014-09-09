package example

import com.typesafe.config.ConfigFactory

trait Settings {
  val conf = ConfigFactory.load
  val host = conf.getString("HOST")
  val user = conf.getString("USER")
  val password = conf.getString("PASSWORD")
  val ns = conf.getString("NS")
  val proj = conf.getString("PROJ")
  val tmpDir = conf.getString("TMPDIR")
}