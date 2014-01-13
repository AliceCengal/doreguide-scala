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

class PlaceDetailer extends Activity
    with ActivityUtil {

  import PlaceDetailer._

  var image: ImageView = null
  var placeName: TextView = null
  var placeDesc: TextView = null
  
  var cont: Actor = new Controller(this).start()
  
  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(R.layout.place_detailer)

    image = component[ImageView](R.id.imageView1)
    placeName = textView(R.id.textView1)
    placeDesc = textView(R.id.textView2)
    
    val id = getIntent().getIntExtra(PLACE_ID_EXTRA, 1)
    cont ! FetchMe(id)
  }

  def setPlace(plc: Place): Unit = {
    onUi {
      
    }
  }
  
  def setImage(img: Bitmap): Unit = {
    onUi {
      
    }
  }
  
}

object PlaceDetailer {

  val PLACE_ID_EXTRA = "placeIdExtra"

  def open(ctx: Context, placeId: Int): Unit = {
    val i = new Intent(ctx, PlaceDetailer.getClass())
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
                mediaIds(0).location.toInt);
            
            activity.setPlace(plc)
            
          case Image(url, img) => activity.setImage(img)
          case a: Any => debug("Message unknown: " + a)
        }
      }

    }

    override def exceptionHandler = {
      case e => error(e.getMessage())
    }

  }
}

