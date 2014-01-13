package edu.vanderbilt.vm.doreguide.utils

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.app.Fragment
import android.widget.ListView

trait ViewUtil {

  def click(v: View)(block: => Unit): Unit = {
    v.setOnClickListener(new OnClickListener() {
      override def onClick(v: View) { block }
    })
  }

  def run(block: => Unit): Runnable = {
    new Runnable() {
      override def run() = block
    }
  }

  def onUi(block: => Unit): Unit

  def component[T](id: Int): T

  def button(id: Int) = component[Button](id)

  def textView(id: Int) = component[TextView](id)
  
  def listView(id: Int) = component[ListView](id)
}

/**
 * Utils to interact with an android Activity.
 * Find components with a Type T by id.
 */
trait ActivityUtil extends ViewUtil {
  self: Activity =>

  override def component[T](id: Int) =
    self.findViewById(id).asInstanceOf[T]

  override def onUi(block: => Unit) {
    self.runOnUiThread(run(block))
  }

}

trait FragmentUtil extends ViewUtil {
  self: Fragment =>

  override def onUi(block: => Unit): Unit = {
    self.getActivity().runOnUiThread(run(block))
  }

  override def component[T](id: Int): T = {
    self.getView().findViewById(id).asInstanceOf[T]
  }
}












