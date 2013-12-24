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

class PlaceListFrag extends Fragment 
    with Actor
    with MessageUtil
    with LogUtil {

  private lazy val mView: ListView = new ListView(getActivity)
  private var mPlaceList: List[Place] = Nil
  
  override def onCreateView(
      inflater: LayoutInflater, 
      container: ViewGroup, 
      saved: Bundle): View = mView;
  
  override def onActivityCreated(saved: Bundle) {
    super.onActivityCreated(saved)
    setHasOptionsMenu(true)
    request(Dore.placeServer) { PlaceServer.GetAllPlaces }
  }
  
  override def act: Unit = {
    loop {
      react {
        
        case PlaceList(list) => { debug("received place list") }
        
      }
    }
  }
  
  override def logId = "DoreGuide::PlaceListFrag";
  
  override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.place_list, menu);
  }
}
