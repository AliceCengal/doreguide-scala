package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.URL

class TourServer extends Actor with LogUtil {

  def logId = "DoreGuide::TourServer"
  private var mTourData: List[Tour] = List.empty
    
  def act() {
    loop {
      react {
        case Initialize(ctx) => initialize
      }
    }
  }

  def initialize {

    val reader = new JsonReader(
        new InputStreamReader(
            new URL(TourServer.rawDataUrl).openConnection.getInputStream))
    
    reader.beginArray()
    while(reader.hasNext())
      
    
  }
}

object TourServer {
  val rawDataUrl = "https://raw.github.com/VandyMobile/guide-android/master/GuideAndroid/assets/tours.json"
}