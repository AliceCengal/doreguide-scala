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

  val rawDataUrl = "https://raw.github.com/VandyMobile/guide-android/master/GuideAndroid/assets/places.json"
  
  val mList = List(
      Place(0, 0, List.empty, "Mock Place 1", "", "", List.empty, 1),
      Place(0, 0, List.empty, "Mock Place 2", "", "", List.empty, 2),
      Place(0, 0, List.empty, "Mock Place 3", "", "", List.empty, 3),
      Place(0, 0, List.empty, "Mock Place 4", "", "", List.empty, 4));
  
  var mPlaceData: List[Place] = List.empty
  
  private var count: Int = 0
  
  def logId = "DoreWay::Server"
  
  def act() {
    
    initializeData
    
    loop {
      react {
        case Incre	=> 
          count = count + 1
          debug("Incre received")
          
        case Request(requester, message) =>
            message match {
              case Get	=> 
                requester ! Count(count)
                debug("Get received in a Request")
              case GetPlaceWithId(id) =>
                if (id >= 0 && id < 9999 && mPlaceData.length != 0) 
                  requester ! PlaceList(List(mPlaceData(id)))
            }
        case a:Any		=> { debug("Message not understood: " + a) }
      }
    }
  }
  
  def initializeData {
    
    val reader = new JsonReader(
        new InputStreamReader(
            new URL(rawDataUrl).openConnection().getInputStream))
    
    reader.beginArray()
    while(reader.hasNext()) {
      mPlaceData = Place.buildFromJson(reader) :: mPlaceData
    }
    reader.endArray()
  }
  
}

case class Incre
case class Get
case class Count(c: Int)
case class GetPlaceWithId(id: Int)














