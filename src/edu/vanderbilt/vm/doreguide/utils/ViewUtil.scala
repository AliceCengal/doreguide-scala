package edu.vanderbilt.vm.doreguide.utils

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import android.view.View
import android.view.View.OnClickListener


trait ViewUtil {

  
  
}

/**
 * Utils to interact with an android Activity.
 * Find components with a Type T by id.
 * Access to the main application through app.
 */
trait ActivityUtil {
  self: Activity =>

  def app: App = self.getApplication.asInstanceOf[App]

  def button(id: Int) =
    component[Button](id)

  def textView(id: Int) =
    component[TextView](id)

  def component[T](id: Int) =
    self.findViewById(id).asInstanceOf[T]

  def click(v: View)(block: => Unit) {
    v.setOnClickListener(new OnClickListener() {
      override def onClick(v: View) { block }
    })
  }
  
  def onUi(block: => Unit) {
    self.runOnUiThread(new Runnable() {
      override def run() { block }
    })
  }

}














