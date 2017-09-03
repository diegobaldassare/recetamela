import java.net.InetSocketAddress

import play.sbt.PlayRunHook
import sbt._

object UIBuild {
  def apply(base: File): PlayRunHook = {
    object UIBuildHook extends PlayRunHook {

      var processOne: Option[Process] = None
      var processTwo: Option[Process] = None

      var npmInstall: String = "npm install"
      var npmRunBuild: String = "npm run build --"
      var ngServe: String = "ng serve"

      if (System.getProperty("os.name").toLowerCase().contains("win")){
        npmInstall = "cmd /c" + npmInstall
        npmRunBuild = "cmd /c" + npmRunBuild
        ngServe = "cmd /c" + ngServe
      }

      override def beforeStarted(): Unit = {
        if (!(base / "ui" / "node_modules").exists()) Process(npmInstall, base / "ui").!
      }

      override def afterStarted(addr: InetSocketAddress): Unit = {
        processOne = Option(
          Process(npmRunBuild, base / "ui").run
        )
        processTwo = Option(
          Process(ngServe, base / "ui").run
        )
      }

      override def afterStopped(): Unit = {
        processOne.foreach(_.destroy())
        processOne = None
        processTwo.foreach(_.destroy())
        processTwo = None
      }

    }

    UIBuildHook
  }
}