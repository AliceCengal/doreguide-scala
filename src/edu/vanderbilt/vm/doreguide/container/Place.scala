package edu.vanderbilt.vm.doreguide.container

import com.google.gson.stream.JsonReader

case class Place( latitude: Double
			    , longitude: Double
			    , medias: List[MediaLocation]
			    , name: String
			    , description: String
			    , hours: String
			    , categories: List[PlaceCategory]
			    , uniqueId: Int
			    ) extends DataObject {

  override def toString: String = "{ id: " + uniqueId + ", name: " + name + " }"
  override def hashCode: Int = uniqueId
  override def equals(other: Any): Boolean =
    if (!other.isInstanceOf[Place]) false
    else uniqueId == other.asInstanceOf[Place].uniqueId

}

object Place {
  val DEFAULT_ID    = -1
  val MAX_PLACE_ID  = 9999 // Largest Id allowed for a place

  val TAG_ID        = "id"
  val TAG_NAME      = "name"
  val TAG_DESC      = "placeDescription"
  val TAG_CAT       = "category"
  val TAG_HOURS     = "hours"
  val TAG_IMAGE     = "imagePath"
  val TAG_AUDIO     = "audioPath"
  val TAG_VIDEO     = "videoPath"
  val TAG_LAT       = "latitude"
  val TAG_LON       = "longitude"
  val TAG_IMAGEIDS  = "imageIds"

  def builder: PlaceBuilder = new IPlaceBuilder
    
  def buildFromJson(reader: JsonReader): Place = {
    val bldr = builder
    reader.beginObject()
    while (reader.hasNext) {
      val n = reader.nextName()
      n match {
        case TAG_ID   => bldr.setId(reader.nextInt())
        case TAG_NAME => bldr.setName(reader.nextString())
        case TAG_DESC => bldr.setDescription(reader.nextString())
        case TAG_CAT =>
          reader.beginArray()
          while (reader.hasNext)
            bldr.addCategory(PlaceCategory.fromName(reader.nextString()))
          reader.endArray()
        case TAG_HOURS => bldr.setHours(reader.nextString())
        case TAG_IMAGE => 
          // We no longer want to use this.
          // bldr.addMedia(MediaLocation(ImageMedia, reader.nextString()))
          reader.skipValue()
        case TAG_AUDIO => bldr.addMedia(MediaLocation(AudioMedia, reader.nextString()))
        case TAG_VIDEO => bldr.addMedia(MediaLocation(VideoMedia, reader.nextString()))
        case TAG_LAT   => bldr.setLatitude(reader.nextDouble())
        case TAG_LON   => bldr.setLongitude(reader.nextDouble())
        case TAG_IMAGEIDS =>
          reader.beginArray()
          while (reader.hasNext())
            bldr.addMedia(MediaLocation(ImageId, reader.nextInt().toString()))
          reader.endArray()
        case _ => reader.skipValue()
      }
    }
    reader.endObject()
    bldr.build
  }

}

trait PlaceBuilder {
  def setLatitude(lat: Double)
  def setLongitude(lon: Double)
  def addMedia(media: MediaLocation)
  def removeMedia(media: MediaLocation)
  def setName(name: String)
  def setDescription(desc: String)
  def setHours(hours: String)
  def addCategory(cat: PlaceCategory)
  def setId(id: Int)
  def build: Place
}

private class IPlaceBuilder extends PlaceBuilder {

  var latitude: Double                  = 0
  var longitude: Double                 = 0
  var medias: List[MediaLocation]       = List.empty
  var name: String                      = ""
  var description: String               = ""
  var hours: String                     = ""
  var categories: List[PlaceCategory]   = List.empty
  var uniqueId: Int                     = Place.DEFAULT_ID

  def setLatitude(lat: Double)          { latitude = lat }
  def setLongitude(lon: Double)         { longitude = lon }
  def addMedia(media: MediaLocation)    { medias = media :: medias }
  def removeMedia(media: MediaLocation) { medias = for (m <- medias; if (m != media)) yield m }
  def setName(n: String)                { name = n }
  def setDescription(desc: String)      { description = desc }
  def setHours(h: String)               { hours = h }
  def addCategory(cat: PlaceCategory)   { categories = cat :: categories }
  def setId(id: Int)                    { uniqueId = id }

  def build: Place = {
    if (uniqueId == Place.DEFAULT_ID) throw new IllegalStateException
    else Place( latitude
              , longitude
              , medias
              , name
              , description
              , hours
              , categories
              , uniqueId
              )
  }
}

sealed trait PlaceCategory {
  def name:     String
  def id:       Int
}

object PlaceCategory {

  def fromName(name: String): PlaceCategory = {
    name match {
      case "Residence Hall" => ResidenceHall
      case "Recreation"     => Recreation
      case "Dining"         => Dining
      case "Academics"      => Academics
      case "Everything"     => Everything
      case "Facility"       => Facility
      case "Medical"        => Medical
      case "Local"          => Local
      case "Athletics"      => Athletics
      case "Greek Life"     => GreekLife
      case "Student Life"   => StudentLife
      case "Library"        => Library
      case _                => Others
    }
  }
}

case object ResidenceHall   extends PlaceCategory { def name = "Residence Hall";    def id = 0  }
case object Recreation      extends PlaceCategory { def name = "Recreation";        def id = 1  }
case object Dining          extends PlaceCategory { def name = "Dining";            def id = 2  }
case object Academics       extends PlaceCategory { def name = "Academics";         def id = 3  }
case object Everything      extends PlaceCategory { def name = "Everything";        def id = 4  }
case object Facility        extends PlaceCategory { def name = "Facility";          def id = 5  }
case object Medical         extends PlaceCategory { def name = "Medical";           def id = 6  }
case object Local           extends PlaceCategory { def name = "Local";             def id = 7  }
case object Athletics       extends PlaceCategory { def name = "Athletics";         def id = 8  }
case object GreekLife       extends PlaceCategory { def name = "Greek Life";        def id = 9  }
case object StudentLife     extends PlaceCategory { def name = "Student Life";      def id = 10 }
case object Library         extends PlaceCategory { def name = "Library";           def id = 11 }
case object Others          extends PlaceCategory { def name = "Others";            def id = 12 }







