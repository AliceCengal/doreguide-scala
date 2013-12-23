package edu.vanderbilt.vm.doreway

import scala.actors.Actor
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.URL

class NodeServer extends Actor with LogUtil {

  def logId = "DoreWay::NodeManager"

  def act() {
    loop {
      react {
        case Initialize(ctx) => initialize
        case a: Any          => debug("Message not understood: " + a)
      }
    }
  }

  def initialize {
    val reader = new JsonReader(
      new InputStreamReader(
        (new URL(NodeServer.rawDataUrl)).openConnection().getInputStream()))

  }
}

object NodeServer {

  case class GetVertexWithId(id: Int)
  val rawDataUrl = "https://raw.github.com/VandyMobile/guide-android/master/GuideAndroid/assets/nodes.json"
}