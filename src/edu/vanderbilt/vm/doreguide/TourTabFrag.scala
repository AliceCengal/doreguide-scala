package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import android.app.Fragment
import edu.vanderbilt.vm.doreguide.container.Tour
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import edu.vanderbilt.vm.doreguide.utils._
import android.widget.ListView
import edu.vanderbilt.vm.doreguide.views.DataAdapter
import edu.vanderbilt.vm.doreguide.views.TourView
import edu.vanderbilt.vm.doreguide.services._

class TourTabFrag(val cont: Actor) extends Fragment
    with FragmentUtil {

  private var mTourList: List[Tour] = List.empty

  private var tourView: ListView = null
  
  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            saved: Bundle): View = {
    inflater.inflate(R.layout.place_tab, container, false)
  }

  override def onActivityCreated(saved: Bundle) {
    super.onActivityCreated(saved)
    tourView = listView(R.id.listView1)
    cont ! Start
  }

  def setTourList(list: List[Tour]): Unit = {
    onUi {
      tourView.setAdapter(
          new DataAdapter(
              list,
              TourView.getFactory(getActivity())));
      tourView invalidateViews()
    }
  }
}

class ToursController extends Actor with LogUtil {

  import TourServer._
  import MainController._

  private val frag = new TourTabFrag(this)
  private var mData: List[Tour] = List.empty

  override def logId = "ToursController"

  override def act(): Unit = {
    loop {
      react {
        case Initialize(ctx) => Dore.tourServer ! GetAllTours

        case Start           => frag.setTourList(mData)

        case TourList(list)  => mData = list

        case ShowTab         => //sender ! ShowFragment(frag)

        case _               => debug("Message not understood.")
      }
    }
  }

  override def exceptionHandler = {
    case e => error(e.getMessage())
  }

}



