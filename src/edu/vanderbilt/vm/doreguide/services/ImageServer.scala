package edu.vanderbilt.vm.doreguide.services

import scala.actors.Actor
import android.graphics.Bitmap
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import edu.vanderbilt.vm.doreguide.R
import edu.vanderbilt.vm.doreguide.Initialize

/**
 * Downloads and caches images on request.
 */
class ImageServer extends Actor with LogUtil {

  import ImageServer._
  
  var defaultImage: Bitmap = null
  
  override def logId = "DoreGuide::ImageServer"
  
  override def act(): Unit = {
    loop { react {
      case Initialize(ctx) => initialize(ctx)
      case DispatchImage(url) => { sender ! Image(url, defaultImage) }
      case _ => { debug("Message Not Understood") }
    } }
  }
  
  
  def initialize(ctx: Context): Unit = {
    defaultImage = ctx.
      getResources().
      getDrawable(R.drawable.ic_launcher).
      asInstanceOf[BitmapDrawable].
      getBitmap()
  }
}

object ImageServer {
  case class DispatchImage(url: String)
  case class Image(url: String, img: Bitmap)
  
  
}