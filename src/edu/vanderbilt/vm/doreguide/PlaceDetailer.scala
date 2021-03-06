package edu.vanderbilt.vm.doreguide

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import scala.actors.Actor
import edu.vanderbilt.vm.doreguide.utils._
import android.widget._
import edu.vanderbilt.vm.doreguide.container._
import android.graphics.Bitmap
import edu.vanderbilt.vm.doreguide.services._
import android.view.View
import android.text.Html

class PlaceDetailer extends Activity
    with ActivityUtil {

  import PlaceDetailer._

  var image: ImageView = null
  var placeName: TextView = null
  var placeDesc: TextView = null
  var placeCat: TextView = null
  var placeHours: TextView = null
  var placeGeo: TextView = null
  var placeMedias: TextView = null
  
  var cont: Actor = new Controller(this).start()
  
  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(R.layout.place_detailer)

    image = component[ImageView](R.id.imageView1)
    placeName = textView(R.id.textView1)
    placeDesc = textView(R.id.textView2)
    placeCat = textView(R.id.textView3)
    placeHours = textView(R.id.textView4)
    placeGeo = textView(R.id.textView5)
    placeMedias = textView(R.id.textView6)
    
    val id = getIntent().getIntExtra(PLACE_ID_EXTRA, 1)
    cont ! FetchMe(id)
  }

  def setPlace(plc: Place): Unit = {
    onUi {
      placeName.setText(plc.name)
      placeDesc.setText(Html.fromHtml(plc.description));
      
      placeCat.setText(Html.fromHtml("<b>Category:</b> " + plc.categories.map(c => c.name).mkString(", ")))
      placeHours.setText(Html.fromHtml("<b>Hours:</b> " + plc.hours))
      placeGeo.setText(Html.fromHtml("<b>Geopoint:</b> " + plc.latitude + ", " + plc.longitude))
      placeMedias.setText(Html.fromHtml("<em>" + plc.medias.length + " medias available." + "</em>"))
    }
  }
  
  def setImage(img: Bitmap): Unit = {
    onUi {
      image.setImageBitmap(img)
    }
  }
  
}

object PlaceDetailer {

  val PLACE_ID_EXTRA = "placeIdExtra"

  /**
   * Open the Detail page using this method. Pass in the id of the Place that is to be detailed.
   */
  def open(ctx: Context, placeId: Int): Unit = {
    val i = new Intent(ctx, classOf[PlaceDetailer])
    i.putExtra(PLACE_ID_EXTRA, placeId)
    ctx startActivity i
  }

  private case class FetchMe(id: Int)
  private case class AddAgenda(id: Int)
  
  private class Controller(val activity: PlaceDetailer) extends Actor with LogUtil {

    import PlaceServer._
    import ImageServer._

    override def logId = "PlaceDetailerController"

    override def act(): Unit = {
      loop {
        react {
          case FetchMe(id) =>
            Dore.placeServer ! GetPlaceWithId(id)

          case PlaceList(list) =>
            val plc = list.head
            val mediaIds = plc.medias.filter((m) => m.mediatype == ImageId)
            if (mediaIds.length > 0)
              Dore.imageServer ! DispatchImageFromId(
                mediaIds.head.location.toInt);
            
            activity setPlace plc

          case Image(url, img) => 
            activity.setImage(img)
            debug("Setting image")
          case a: Any          => debug("Message unknown: " + a)
        }
      }

    }

    override def exceptionHandler = {
      case e => error(e.getMessage())
    }

  }
}

