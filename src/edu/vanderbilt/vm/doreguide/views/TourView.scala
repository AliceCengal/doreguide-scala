package edu.vanderbilt.vm.doreguide.views

import android.widget.TextView
import android.content.Context
import android.view.ViewGroup
import android.view.View
import edu.vanderbilt.vm.doreguide.container.Tour

/**
 * Defines how a Tour object should present itself in a ListView.
 */
class TourView(ctx: Context) extends ViewHolder {

  private val mView = new TextView(ctx)
  
  override def getView = mView
  
  override def updateView(
      item: Any, 
      position: Int, 
      convView: View, 
      parent: ViewGroup): Unit = {
    mView.setText(item.asInstanceOf[Tour].name)
  }
}

object TourView {
  private class TourViewFactory(val ctx: Context) extends ViewHolderFactory {
    def createHolder() = new TourView(ctx)
  }
  
  def getFactory(ctx: Context): ViewHolderFactory = new TourViewFactory(ctx)
}