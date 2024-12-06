package utils

import java.io.File

object Constants {
  val projectRoot: String = new File(".").getCanonicalPath
  val allowedExtensions: Seq[String] = Seq("jpg", "jpeg", "png", "gif")
}