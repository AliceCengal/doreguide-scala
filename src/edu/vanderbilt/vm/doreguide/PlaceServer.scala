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
import edu.vanderbilt.vm.doreguide.utils.LogUtil

class PlaceServer extends Actor
    with LogUtil {

  import PlaceServer._
  
  private var mPlaceData: List[Place] = List.empty
  private var count: Int = 0

  override def logId = "DoreGuide::PlaceServer";

  override def act() {
    loop {
      react {
        case Initialize(ctx) => initializeData
        case Incre =>
          count = count + 1
          debug("Incre received")

        case Get =>
          debug("Get received bare")
          sender ! Count(count)
        
        case GetAllPlaces =>
          sender ! PlaceList(mPlaceData)
          debug("Sending all " + mPlaceData.length + " Places to " + sender.toString())
          
        case GetPlaceWithId(id) =>
          debug("Sending Place with id " + id)
          val result = mPlaceData.filter(p => p.uniqueId == id);
          debug("There are " + result.length + "matches. Expected 1 match")
          sender ! PlaceList(result);
          
        case GetPlacesIdRange(ids) =>
          debug("Sending Places with ids: " + ids.mkString)
          val result = for (
              id <- ids;
              place <- mPlaceData
              if (place.uniqueId == id))
            yield place;
          debug("There are " + result.length + "matches. Expected" + ids.length + "matches")
          sender ! PlaceList(result)

        case _ => { debug("Message not understood") }
      }
    }
  }
  
  override def exceptionHandler = {
    case e => error(e.getMessage())
  }

  private def initializeData {
    val reader = new JsonReader(
      new InputStreamReader(
        (new URL(rawDataUrl)).openConnection().getInputStream))

    mPlaceData = List.empty
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
  case class GetPlacesIdRange(ids: List[Int])
  case object GetAllPlaces
  val rawDataUrl = "https://raw.github.com/AliceCengal/vanderbilt-data/master/places.json"
}















