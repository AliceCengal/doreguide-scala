package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import android.app.Fragment
import edu.vanderbilt.vm.doreguide.container.Place
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import android.widget.ListView
import edu.vanderbilt.vm.doreguide.views._
import edu.vanderbilt.vm.doreguide.services._
import android.widget.ImageView
import android.widget.TextView
import android.widget.ImageButton
import edu.vanderbilt.vm.doreguide.utils.Listenable._
import android.app.Activity
import android.graphics.Bitmap
import android.text.Html
import edu.vanderbilt.vm.doreguide.container._
import edu.vanderbilt.vm.doreguide.utils._
import android.widget.LinearLayout


class AgendaTabFrag(val cont: Actor) extends Fragment
    with FragmentUtil
    with LogUtil {

  private var mAgenda: List[Place] = List.empty

  var agendaList: ListView = null
  var currentView: LinearLayout = null
  var placeImage: ImageView = null
  var placeText: TextView = null
  var editBtn: ImageButton = null
  var deleteBtn: ImageButton = null
  
  val DESC_LENGTH = 100
  
  override def logId = "AgendaTabFrag"

  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            saved: Bundle): View = {
    inflater.inflate(R.layout.agenda_tab, container, false)
  }

  override def onActivityCreated(saved: Bundle) {
    super.onActivityCreated(saved)
    
    agendaList = listView(R.id.listView1)
    currentView = component[LinearLayout](R.id.linearLayout1)
    placeImage = component[ImageView](R.id.imageView1)
    placeText = textView(R.id.textView2)
    editBtn = component[ImageButton](R.id.imageButton1)
    deleteBtn = component[ImageButton](R.id.imageButton2)

    cont ! Start
  }

  /* override def onResume() = {
    super.onResume()
    debug("onResume callback")
  }
  
  override def onPause() = {
    super.onPause()
    debug("onPause callback")
  } */
  
  override def onStop() = {
    super.onStop()
    debug("onStop callback")
    cont ! Stop
  }
  
  /* override def onAttach(act: Activity) = {
    super.onAttach(act)
    debug("onAdded callback")
  }
  
  override def onDetach() = {
    super.onDetach()
    debug("onDetach callback")
  } */

  def fillCurrentPlaceBox(plc: Place): Unit = {
    onUi {
      placeText.setText(Html.fromHtml(
          "<b>" + plc.name + "</b> " +
          (if (plc.description.length() > DESC_LENGTH) 
            plc.description.substring(0, DESC_LENGTH) + "..." 
            else plc.description)));
      
      click(currentView) { PlaceDetailer.open(getActivity(), plc.uniqueId) }
    }
  }

  def setImage(img: Bitmap) = {
    onUi {
      placeImage.setImageBitmap(img)
    }
  }

  def setAgendaList(plcs: List[Place]) = {
    onUi {
      agendaList.setAdapter(
        new DataAdapter(
          plcs,
          PlaceView.getFactory(getActivity())));
      agendaList.invalidateViews()
    }
  }
}

class AgendaController extends Actor with LogUtil {

  import Geomancer._
  import MainController._
  import PlaceServer._
  import AgendaManager._
  import ImageServer._

  private val frag = new AgendaTabFrag(this)
  private var mLocation = ""
  private var currentPlace: Place = null

  override def logId = "AgendaController"

  override def act(): Unit = {
    loop {
      react {
        case Initialize(ctx) => {}
        
        case Start =>
          Dore.geomancer ! GetLocation
          Dore.geomancer ! AddListener(this)
          Dore.agendaManager ! GetUserAgenda
          debug("Received Start signal from fragment")

        case CurrentLoc(lat, lng) =>
          Dore.placeServer ! FindClosestPlace(lat, lng)
          //debug("Received Current location")

        case Agenda(ids) =>
          Dore.placeServer ! GetPlacesIdRange(ids)
          debug("Received Agenda")

        case ClosestPlace(plc) =>
          if (plc != currentPlace) {
            currentPlace = plc
            frag.fillCurrentPlaceBox(plc)

            for (media <- extractFirstImage(plc.medias))
              Dore.imageServer ! (
                media.mediatype match {
                  case ImageMedia => DispatchImage(media.location)
                  case ImageId    => DispatchImageFromId(media.location.toInt)
                })
          }
          //debug("Received closest place: " + plc)

        case Image(url, img) =>
          frag.setImage(img)
          debug("Received image for " + url)

        case PlaceList(list) =>
          frag.setAgendaList(list)
          debug("Received Agenda list with length: " + list.length)

        case ShowTab =>
          sender ! ShowFragment(frag)
          debug("Sending Fragment for display")
          
        case Stop =>
          Dore.geomancer ! RemoveListener(this)
          
        case a: Any => debug("Message not understood: " + a)
      }
    }
  }

  override def exceptionHandler = {
    case e => error(e.getMessage())
  }
  
  private def extractFirstImage(mediaList: List[MediaLocation]): Option[MediaLocation] = {
    val res = mediaList.
      filter(m =>
        m.mediatype == ImageMedia ||
        m.mediatype == ImageId)
        
    if (res.length > 0) Some(res.head)
    else None
  }

}




