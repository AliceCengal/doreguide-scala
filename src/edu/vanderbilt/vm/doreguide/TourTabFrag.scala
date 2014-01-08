package edu.vanderbilt.vm.doreguide

import scala.actors.Actor
import android.app.Fragment
import edu.vanderbilt.vm.doreguide.container.Tour
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import edu.vanderbilt.vm.doreguide.utils.ViewUtil
import edu.vanderbilt.vm.doreguide.utils.LogUtil
import android.widget.ListView
import edu.vanderbilt.vm.doreguide.views.DataAdapter
import edu.vanderbilt.vm.doreguide.views.TourView

class TourTabFrag extends Fragment with ViewUtil {

  private var mTourList: List[Tour] = List.empty

  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            saved: Bundle): View = {
    inflater.inflate(R.layout.place_tab, container, false)
  }

  override def onActivityCreated(saved: Bundle) {
    super.onActivityCreated(saved)

    inGroup(getView()) {
      case (v: ListView, R.id.listview1) =>
        v.setAdapter(new DataAdapter(mTourList, TourView.getFactory(getActivity())))
      case _ => {}
    }
  }

  def setTourList(list: List[Tour]): Unit = {
    mTourList = list
  }
}

class ToursController extends Actor with LogUtil {

  import TourServer._
  import MainController._

  private val frag = new TourTabFrag()
  private var mData: List[Tour] = List.empty

  override def logId = "ToursController"

  override def act(): Unit = {
    loop {
      react {
        case Initialize(ctx) =>
          Dore.placeServer ! GetAllTours

        case TourList(list) =>
          mData = list

        case ShowTab =>
          frag.setTourList(mData)
          sender ! ShowFragment(frag)

        case _ => {}
      }
    }
  }

  override def exceptionHandler = {
    case e => e.printStackTrace()
  }

}



