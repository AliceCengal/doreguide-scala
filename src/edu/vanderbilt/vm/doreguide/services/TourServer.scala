package edu.vanderbilt.vm.doreguide.services

import scala.actors.Actor
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.URL
import edu.vanderbilt.vm.doreguide.container.Tour
import edu.vanderbilt.vm.doreguide.utils.LogUtil

class TourServer extends Actor with LogUtil {

  import TourServer._
  
  private var mTourData: List[Tour] = List.empty
  
  override def logId = "DoreGuide::TourServer"
  
  override def act() {
    loop {
      react {
        case GetAllTours => 
          sender ! TourList(mTourData)
          debug("Sending all Tours")
          
        case GetTourWithId(id) =>
          debug("Sending Tour with id " + id)
          val result = mTourData.filter(p => p.uniqueId == id)
          debug("Found " + result.length + ", expected 1 match.")
          sender ! TourList(result)
          
        case GetTourInRange(ids) =>
          debug("Sending Tours with ids: " + ids.mkString)
          val result = for (
              id <- ids;
              tour <- mTourData
              if (tour.uniqueId == id))
            yield tour;
          debug("Found " + result.length + ", expected " + ids.length + " matches.")
          sender ! TourList(result)
        case Initialize(ctx) => initialize
      }
    }
  }

  override def exceptionHandler = {
    case e => error(e.getMessage())
  }
  
  private def initialize {

    val reader = new JsonReader(
        new InputStreamReader(
            new URL(TourServer.rawDataUrl).openConnection.getInputStream))
    
    mTourData = List.empty
    reader.beginArray()
    while(reader.hasNext()) { mTourData = Tour.buildFromJson(reader) :: mTourData }
    reader.endArray()
    
  }
}

object TourServer {
  case class GetTourWithId(id: Int)
  case class GetTourInRange(ids: List[Int])
  case object GetAllTours
  val rawDataUrl = "https://raw.github.com/AliceCengal/vanderbilt-data/master/tours.json"
}
