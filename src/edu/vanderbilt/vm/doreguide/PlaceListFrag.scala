package edu.vanderbilt.vm.doreguide

import android.app.Fragment
import android.widget.ListView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import scala.actors.Actor
import edu.vanderbilt.vm.doreguide.utils.MessageUtil
import android.view.Menu
import android.view.MenuInflater
import edu.vanderbilt.vm.doreguide.container.Place
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import android.widget.TextView
import android.app.DialogFragment
import edu.vanderbilt.vm.doreguide.views.PlaceView
import edu.vanderbilt.vm.doreguide.views.DataAdapter
import edu.vanderbilt.vm.doreguide.utils.ViewUtil
import android.widget.Toast
import edu.vanderbilt.vm.doreguide.services.PlaceList
import edu.vanderbilt.vm.doreguide.services.Initialize
import edu.vanderbilt.vm.doreguide.services.Dore
import edu.vanderbilt.vm.doreguide.services.PlaceServer

class PlaceListFrag(val controller: Actor) extends Fragment
    with ViewUtil
    with LogUtil {

  private var mPlaceList: List[Place] = Nil

  override def onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    saved: Bundle): View = {
    inflater.inflate(R.layout.place_tab, container, false)
  }

  override def onActivityCreated(saved: Bundle) {
    super.onActivityCreated(saved)
    
    inGroup(getView()) {
      case (v: ListView, R.id.listview1) =>
        v.setAdapter(new DataAdapter(
          mPlaceList,
          PlaceView.getFactory(getActivity())))
            
      case (v, R.id.btn1) =>
        click(v) {
          Toast.
            makeText(
              getActivity(),
              "Button1 pressed",
              Toast.LENGTH_SHORT).
            show()
        }
      case (v, R.id.btn2) =>
        click(v) {
          Toast.
            makeText(
              getActivity(),
              "Button2 pressed",
              Toast.LENGTH_SHORT).
            show()
        }
      case _ => {}
    }

  }

  override def logId = "DoreGuide::PlaceListFrag";

  def setPlaceList(pl: List[Place]): Unit = {
    mPlaceList = pl;
  }

}

object PlaceController {
  case object GetMarkedPlaces
}

class PlaceController extends Actor
    with LogUtil {

  import MainController._
  import PlaceServer._

  private val frag = new PlaceListFrag(this)
  private var mData: List[Place] = List.empty

  override def logId = "DoreGuide::PlaceController"

  override def act(): Unit = {
    loop {
      react {
        case Initialize(ctx) =>
          debug("Initialization.")
          Dore.placeServer ! GetAllPlaces

        case PlaceList(list) =>
          mData = list
          debug("Received data")

        case ShowTab =>
          frag.setPlaceList(mData)
          sender ! ShowFragment(frag)
          debug("Sending Fragment for display")

        case HideTab => debug("HideTab message received.")
        case _       => debug("Message not understood")
      }
    }
  }

  override def exceptionHandler = {
    case e => error(e.getMessage())
  }
}