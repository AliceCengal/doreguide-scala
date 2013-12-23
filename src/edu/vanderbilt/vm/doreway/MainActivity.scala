package edu.vanderbilt.vm.doreway

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import com.google.gson.stream.JsonReader
import java.io.StringReader
import android.widget.TextView
import android.app.ActionBar
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import scala.actors.Actor
import android.util.Log

class MainActivity extends Activity with ActivityUtil with Reactive {
  
  val mList = List(
	Place(0, 0, List.empty, "Mock Place 1", "", "", List.empty, 1),
	Place(0, 0, List.empty, "Mock Place 2", "", "", List.empty, 2),
	Place(0, 0, List.empty, "Mock Place 3", "", "", List.empty, 3),
	Place(0, 0, List.empty, "Mock Place 4", "", "", List.empty, 4))
  
  lazy val mAction = getActionBar
  lazy val mView = new TextView(this)
  val mCounter: Actor = {
    val c = new Server()
    c.start()
    c
  }
  
  override def onCreate(saved: Bundle) {
	super.onCreate(saved)
	
    mView.setText("Hello")
    setContentView(mView)
    
    setupActionBar
    
    click(mView) {
	  mCounter ! Incre
	  mCounter ! Get(this)
	}

    //val reader = new JsonReader(new StringReader("Hello"))
  }
  
  onReact {
    case Count(count) => onUi {
      Log.d("DoreWay::MainActivity", "Count received")
      mView.setText("Count: " + count)
    }
    
  }
  
  private def setupActionBar {
    mAction setNavigationMode			ActionBar.NAVIGATION_MODE_TABS
    mAction setDisplayShowTitleEnabled	true
    mAction setBackgroundDrawable		DoreWay.DECENT_GOLD
    mAction setSplitBackgroundDrawable	DoreWay.DECENT_GOLD
    mAction setTitle					"Vanderbilt University"
  }

}