package edu.vanderbilt.vm.doreguide.container

import com.google.gson.stream.JsonReader

case class Tour( places: List[Place]
               , timeRequired: String
               , distance: Double
               , description: String
               , name: String
               , uniqueId: Int
               , medias: List[MediaLocation]
               ) {  
  override def toString = "{ id: " + uniqueId + ", name: " + name + " }"
  override def hashCode: Int = uniqueId
  override def equals(other: Any): Boolean =
    if (!other.isInstanceOf[Tour]) false
    else uniqueId == other.asInstanceOf[Tour].uniqueId

}

object Tour {
  val DEFAULT_ID = -1
  
  val TAG_ID = "id"
  val TAG_NAME = "name"
  val TAG_TIMEREQ = "timeRequired"
  val TAG_PLACES = "placesOnTour"
  val TAG_DESC = "description"
  val TAG_DIST = "distance"
  val TAG_ICON = "iconPath"
  
  def builder: TourBuilder = new ITourBuilder
  
  def buildFromJson(reader: JsonReader): Tour = {
    val bldr = builder
    reader.beginObject()
    while (reader.hasNext()) {
      val n = reader.nextName()
      n match {
        case TAG_ID      => bldr.setId(reader.nextInt())
        case TAG_NAME    => bldr.setName(reader.nextString())
        case TAG_DESC    => bldr.setDescription(reader.nextString())
        case TAG_TIMEREQ => bldr.setTimeReq(reader.nextString())
        case TAG_PLACES  => {}
        case TAG_DIST    => {}
        case TAG_ICON    => {}
      }
    }
    reader.endObject()
    bldr.build
  }
    
}

trait TourBuilder {
  def addPlace(place: Place)
  def setTimeReq(t: String)
  def setDistance(dist: Double)
  def setDescription(d: String)
  def setName(n: String)
  def setId(id: Int)
  def build: Tour
}

private class ITourBuilder extends TourBuilder {
  var places: List[Place]         = List.empty
  var timeRequired: String        = ""
  var distance: Double            = 0
  var description: String         = ""
  var name: String                = ""
  var uniqueId: Int               = Tour.DEFAULT_ID
  var medias: List[MediaLocation] = List.empty
  
  def addPlace(place: Place)    { places = place :: places }
  def setTimeReq(t: String)     { timeRequired = t }
  def setDistance(dist: Double) { distance = dist }
  def setDescription(d: String) { description = d }
  def setName(n: String)        { name = n }
  def setId(id: Int)            { uniqueId = id }
  def build: Tour = {
    if (uniqueId == Tour.DEFAULT_ID) throw new IllegalStateException
    else Tour( places
             , timeRequired
             , distance
             , description
             , name
             , uniqueId
             , medias
             )
  }
}








