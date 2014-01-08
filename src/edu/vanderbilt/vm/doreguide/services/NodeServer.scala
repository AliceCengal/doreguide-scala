package edu.vanderbilt.vm.doreguide.services

import scala.actors.Actor
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.URL
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import edu.vanderbilt.vm.doreguide.Initialize
import edu.vanderbilt.vm.doreguide.services.Initialize

class NodeServer extends Actor with LogUtil {

  def logId = "DoreGuide::NodeManager"

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
  val rawDataUrl = "https://raw.github.com/AliceCengal/vanderbilt-data/master/nodes.json"
}