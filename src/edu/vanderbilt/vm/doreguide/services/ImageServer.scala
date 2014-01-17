package edu.vanderbilt.vm.doreguide.services

import scala.actors.Actor
import android.graphics.Bitmap
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import edu.vanderbilt.vm.doreguide.R
import edu.vanderbilt.vm.doreguide._
import scala.collection.mutable
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.URL
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.DisplayImageOptions

/**
 * Downloads and caches images on request.
 */
class ImageServer extends Actor with LogUtil {

  import ImageServer._
  
  private var defaultImage: Bitmap = null
  private var mUrlMap: mutable.Map[Int, String] = mutable.Map.empty
  private var mImgLoader: ImageLoader = null
  
  override def logId = "DoreGuide::ImageServer"

  override def act(): Unit = {
    loop {
      react {
        case Initialize(ctx) => initialize(ctx)
        case DispatchImage(url) =>
          debug("Received request for URL: " + url)
          sender ! Image(url, mImgLoader.loadImageSync(url))

        case DispatchImageFromId(id) =>
          debug("Received request for id: " + id)
          val urlData = mUrlMap.get(id)
          urlData match {
            case Some(url) =>
              debug("Processing request for URL: " + url)
              sender ! Image(url, mImgLoader.loadImageSync(url))
            case None => sender ! Image("", defaultImage)
          }
        case Goodbye =>

        case a: Any  => debug("Message Not Understood" + a)
      }
    }
  }

  override def exceptionHandler = {
    case e => error(e.getMessage())
  }
  
  def initialize(ctx: Context): Unit = {
    defaultImage = ctx.
      getResources().
      getDrawable(R.drawable.ic_launcher).
      asInstanceOf[BitmapDrawable].
      getBitmap();
    
    val reader = new JsonReader(
      new InputStreamReader(
        (new URL(rawUrl)).openConnection().getInputStream()))
    
    mUrlMap = mutable.Map.empty
    reader.beginArray()
    while (reader.hasNext()) insertTuple(reader)
    reader.endArray()
    reader.close()
    
    debug("Image bank has size: " + mUrlMap.size)
    initImageLoader(ctx)
  }

  def insertTuple(reader: JsonReader) {
    var url = ""
    var id = -1
    reader.beginObject()
    while (reader.hasNext()) {
      val name = reader.nextName()
      name match {
        case "id"  => id = reader.nextInt()
        case "url" => url = reader.nextString()
        case _     => reader.skipValue()
      }
    }
    reader.endObject()
    if (id != -1 && url != "") mUrlMap += ((id, url))
  }
  
  def initImageLoader(ctx: Context) {
    if (mImgLoader == null) {
      mImgLoader = ImageLoader.getInstance();
      val config = new ImageLoaderConfiguration.Builder(ctx)
        .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
          .cacheInMemory(true)
          .cacheOnDisc(true)
          .build())
        .build();

      mImgLoader.init(config);
    }
  }
}

object ImageServer {
  case class DispatchImage(url: String)
  case class DispatchImageFromId(id: Int)
  case class Image(url: String, img: Bitmap)
  
  val rawUrl = "https://raw2.github.com/AliceCengal/vanderbilt-data/master/images.json"
    
}
