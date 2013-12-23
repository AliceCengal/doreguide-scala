package edu.vanderbilt.vm.doreway

import scala.actors.Actor
import java.net.HttpURLConnection
import java.net.URL
import scala.io.Source
import scala.io.Codec
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader

class Server extends Actor
    with LogUtil {

  var mPlaceData: List[Place] = List.empty
  private var count: Int = 0

  override def logId = "DoreWay::Server"

  def act() {
    initializeData
    loop {
      react {
        case Initialize(ctx) =>
          initializeData

        case Server.Incre =>
          count = count + 1
          debug("Incre received")

        case Request(requester, message) =>
          message match {
            case Server.Get =>
              requester ! Server.Count(count)
              debug("Get received in a Request")
            case Server.GetPlaceWithId(id) =>
              if (id >= 0 && id < 9999 && mPlaceData.length != 0)
                requester ! PlaceList(List(mPlaceData(id)))
                
            case Server.GetAllPlaces =>
              requester ! PlaceList(mPlaceData)
          }

        case a: Any => { debug("Message not understood: " + a) }
      }
    }
  }

  def initializeData {

    val reader = new JsonReader(
      new InputStreamReader(
        new URL(Server.rawDataUrl).openConnection().getInputStream))

    reader.beginArray()
    while (reader.hasNext()) {
      mPlaceData = Place.buildFromJson(reader) :: mPlaceData
    }
    reader.endArray()
  }

}

object Server {
  case class Incre
  case class Get
  case class Count(c: Int)
  case class GetPlaceWithId(id: Int)
  case class GetAllPlaces
  val rawDataUrl = "https://raw.github.com/VandyMobile/guide-android/master/GuideAndroid/assets/places.json"
}















