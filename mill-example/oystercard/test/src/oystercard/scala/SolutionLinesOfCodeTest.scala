package oystercard.scala

import org.scalatest.{Matchers, WordSpec}

/**
  * An additional noddy test which just checks our solution isn't too many lines of code
  */
class SolutionLinesOfCodeTest extends WordSpec with Matchers {

  "The scala solution should be readable (we can't really write a test for that) and no bigger than 200 - 300 lines of code" in {
    ProjectMetaData.projectRoot match {
      case Some(projectRootDir) =>
        val actual = ProjectMetaData.scalaLineCountInDir(projectRootDir)
        println(s"The solution contains ${actual} lines of code (excluding white-space/comments)")
        withClue("the line counter didn't seem to pick up any files") {
          actual should not be 0
        }
        actual should be <= 300
      case None => fail("We couldn't seem to determine the project's root directory")
    }
  }

  "The java solution should be readable (we can't really write a test for that) and no bigger than 200 - 300 lines of code" in {
    ProjectMetaData.projectRoot match {
      case Some(projectRootDir) =>
        val actual = ProjectMetaData.javaLineCountInDir(projectRootDir)
        println(s"The java solution contains ${actual} lines of code (excluding white-space/comments)")
        withClue("the line counter didn't seem to pick up any files") {
          actual should not be 0
        }
        actual should be <= 300
      case None => fail("We couldn't seem to determine the project's root directory")
    }
  }


}
