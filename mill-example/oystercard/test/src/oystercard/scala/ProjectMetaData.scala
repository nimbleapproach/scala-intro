package oystercard.scala

import java.nio.file.{Files, Path, Paths}

/**
  * Some functions about the project itself
  */
object ProjectMetaData {

  def projectRoot: Option[Path] = {
    def parents(path: Path): Stream[Path] = Option(path).fold(Stream.empty[Path]) { path => path #:: parents(path.getParent) }

    def isProjectRoot(path: Path) = Files.isDirectory(srcDir(path))

    val thisTestUri = getClass.getProtectionDomain.getCodeSource.getLocation.toURI
    parents(Paths.get(thisTestUri)).find(isProjectRoot)
  }

  def javaLineCountInDir(path: Path, maxDepth : Int = 5): Int = lineCountInDir(path, ".java", maxDepth)
  def scalaLineCountInDir(path: Path, maxDepth : Int = 5): Int = lineCountInDir(path, ".scala", maxDepth)
  private def lineCountInDir(path: Path, suffix : String, maxDepth : Int): Int = {
    def isBlankOrComment(fullLine : String) = {
      val line = fullLine.trim
      line.isEmpty || line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")
    }
    val isSourceLine = (isBlankOrComment _).andThen(_ == false)
    def lineCountForFile(scalaFile: Path): Int = {
      import scala.collection.JavaConverters._
      Files.lines(scalaFile).iterator().asScala.count(isSourceLine)
    }

    val scalaSourceFiles = Files.walk(srcDir(path), maxDepth).filter(_.getFileName.toString.endsWith(suffix))
    scalaSourceFiles.mapToInt(lineCountForFile _).toArray.sum
  }


  private def srcDir(projectRootDir: Path) = projectRootDir.resolve("oystercard/src")
}
