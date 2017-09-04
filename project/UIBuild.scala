
import java.net.InetSocketAddress

import play.sbt.PlayRunHook
import sbt._

object UIBuild {
  def apply(base: File): PlayRunHook = {
    object UIBuildHook extends PlayRunHook {

      var processOne: Option[Process] = None

      var npmInstall: String = "npm install"
      var npmRunBuild: String = "npm run build -- --watch"

      if (System.getProperty("os.name").toLowerCase().contains("win")){
        npmInstall = "cmd /c" + npmInstall
        npmRunBuild = "cmd /c" + npmRunBuild
      }

      override def beforeStarted(): Unit = {
        Process(npmInstall, base / "ui").!
      }

      override def afterStarted(addr: InetSocketAddress): Unit = {
        processOne = Option(
          Process(npmRunBuild, base / "ui").run
        )
      }

      override def afterStopped(): Unit = {
        processOne.foreach(_.destroy())
        processOne = None
      }

    }

    UIBuildHook
  }
}