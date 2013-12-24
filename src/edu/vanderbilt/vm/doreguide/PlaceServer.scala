package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import java.net.HttpURLConnection
import java.net.URL
import scala.io.Source
import scala.io.Codec
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import edu.vanderbilt.vm.doreguide.container.Place
import edu.vanderbilt.vm.doreguide.utils.Request

class PlaceServer extends Actor
    with LogUtil {

  private var mPlaceData: List[Place] = List.empty
  private var count: Int = 0

  override def logId = "DoreGuide::Server";

  override def act() {
    loop {
      react {
        case Initialize(ctx) => initializeData
        case PlaceServer.Incre =>
          count = count + 1
          debug("Incre received")

        case Request(requester, message) =>
          message match {
            case PlaceServer.Get =>
              requester ! PlaceServer.Count(count)
              debug("Get received in a Request")
            case PlaceServer.GetPlaceWithId(id) =>
              if (id >= 0 && id < 9999 && mPlaceData.length != 0)
                requester ! PlaceList(List(mPlaceData(id)))

            case PlaceServer.GetAllPlaces =>
              requester ! PlaceList(mPlaceData)
          }

        case _ => { debug("Message not understood") }
      }
    }
  }

  private def initializeData {
    val reader = new JsonReader(
      new InputStreamReader(
        (new URL(PlaceServer.rawDataUrl)).openConnection().getInputStream))

    reader.beginArray()
    while (reader.hasNext()) {
      mPlaceData = Place.buildFromJson(reader) :: mPlaceData
    }
    reader.endArray()
  }

}

object PlaceServer {
  case class Incre
  case class Get
  case class Count(c: Int)
  case class GetPlaceWithId(id: Int)
  case class GetAllPlaces
  val rawDataUrl = "https://raw.github.com/VandyMobile/guide-android/master/" + 
                   "GuideAndroid/assets/places.json"
}















