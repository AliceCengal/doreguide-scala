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

class TourTabFrag extends Fragment with ViewUtil with LogUtil {

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
}


class ToursController extends Actor {
  override def act(): Unit = {
    loop { react {
      case _ => {}
    } }
  }
  
  override def exceptionHandler = {
    case e => e.printStackTrace()
  }
  
}