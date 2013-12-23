package edu.vanderbilt.vm.doreway

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import android.view.View
import scala.actors.Actor
import android.view.View.OnClickListener
import android.util.Log

/**
 * Converts an activity into an actor
 * to receive events from another actors or services
 * using onReact function
 * 
 * SOURCE: https://github.com/jgoday/sample_android_scala_actor
 */
trait Reactive extends Actor {
    self: Activity with ActivityUtil =>

    private var reactiveFunctions: PartialFunction[Any, Unit] = _

    def act() {
        loop {
            react {
                reactiveFunctions
            }
        }
    }

    def onReact(pf: PartialFunction[Any, Unit]): Unit = {
        startReacting

        reactiveFunctions = pf
    }

    protected def onUi(block : => Unit) {
        self.runOnUiThread(new Runnable() {
            override def run() {
                block
            }
        })
    }

    private def startReacting = {
        this.start
    }
}

/**
 * Utils to interact with an android Activity
 * Find components with a Type T by id
 * Access to the main application throught app
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
    
    def click(v: View) (block: => Unit) {
      v.setOnClickListener(new OnClickListener() {
        override def onClick(v: View) { block }})
    }
    
}

object LogUtil {
  def logEnabled = true
}

trait LogUtil {
  def logId: String
  
  def debug(message: String) { if (LogUtil.logEnabled) Log.d(logId, message) }
  
  def error(message: String) { if (LogUtil.logEnabled) Log.e(logId, message) }
  
  def info(message: String)  { if (LogUtil.logEnabled) Log.i(logId, message) }
}













