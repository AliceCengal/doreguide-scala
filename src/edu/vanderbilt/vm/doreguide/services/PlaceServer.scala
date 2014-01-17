package edu.vanderbilt.vm.doreguide.services

import scala.actors.Actor
import java.net.URL
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import edu.vanderbilt.vm.doreguide.container.Place
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import edu.vanderbilt.vm.doreguide.PlaceList
import edu.vanderbilt.vm.doreguide.Initialize

class PlaceServer extends Actor
    with LogUtil {

  import PlaceServer._
  import edu.vanderbilt.vm.doreguide.services._
  import Geomancer._
  private var mPlaceData: List[Place] = List.empty
  private var count: Int = 0

  override def logId = "DoreGuide::PlaceServer";

  override def act() {
    loop {
      react {
        case Initialize(ctx) => initializeData
        
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
          debug("There are " + result.length + " matches. Expected " + ids.length + " matches")
          sender ! PlaceList(result)

        case FindClosestPlace(lat, lng) =>
          // Sort the Places from closest to farthest, then return
          // the first element of the resulting List, which would be
          // the closest Place to given point.
          val sortedByDistance = mPlaceData.sortWith(
              (a,b) =>
                  calcDistance(a.latitude, a.longitude, lat, lng) <
                  calcDistance(b.latitude, b.longitude, lat, lng))
          
          sender ! ClosestPlace(sortedByDistance.head)
          
        case a: Any => debug("Message not understood: " + a + " from: " + sender)
      }
    }
  }
  
  override def exceptionHandler = {
    case e => error(e.getMessage())
  }

  private def initializeData {
    val reader = new JsonReader(
      new InputStreamReader(
        (new URL(rawDataUrl)).openConnection().getInputStream()))

    mPlaceData = List.empty
    reader.beginArray()
    while (reader.hasNext()) {
      mPlaceData = Place.buildFromJson(reader) :: mPlaceData
    }
    reader.endArray()
    reader.close()
  }

}

object PlaceServer {
  case class GetPlaceWithId(id: Int)
  case class GetPlacesIdRange(ids: List[Int])
  case object GetAllPlaces
  
  case class FindClosestPlace(lat: Double, lng: Double)
  case class ClosestPlace(plc: Place)
  
  val rawDataUrl = "https://raw.github.com/AliceCengal/vanderbilt-data/master/places.json"
}















