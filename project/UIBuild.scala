import java.net.InetSocketAddress

import play.sbt.PlayRunHook
import sbt._

object UIBuild {
  def apply(base: File): PlayRunHook = {
    object UIBuildHook extends PlayRunHook {

      var process: Option[Process] = None

      override def beforeStarted(): Unit = {
        Process("npm install", base / "ui").!
      }

      override def afterStarted(address: InetSocketAddress): Unit = {
        process = Option(
          Process("npm run build -- --watch", base / "ui").run
        )
      }

      override def afterStopped(): Unit = {
        process.foreach(_.destroy())
        process = None
      }

    }

    UIBuildHook
  }
}