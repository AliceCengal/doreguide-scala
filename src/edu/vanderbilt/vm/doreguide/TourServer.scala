package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.URL
import edu.vanderbilt.vm.doreguide.container.Tour

class TourServer extends Actor with LogUtil {

  private var mTourData: List[Tour] = List.empty
  
  override def logId = "DoreGuide::TourServer"
  
  override def act() {
    loop {
      react {
        case Initialize(ctx) => initialize
      }
    }
  }

  private def initialize {

    val reader = new JsonReader(
        new InputStreamReader(
            new URL(TourServer.rawDataUrl).openConnection.getInputStream))
    
    reader.beginArray()
    while(reader.hasNext()) { mTourData = Tour.buildFromJson(reader) :: mTourData }
    reader.endArray()
    
  }
}

object TourServer {
  val rawDataUrl = "https://raw.github.com/VandyMobile/guide-android/master/GuideAndroid/assets/tours.json"
}