package edu.vanderbilt.vm.doreway

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import com.google.gson.stream.JsonReader
import java.io.StringReader
import android.widget.TextView
import android.app.ActionBar

class MainActivity extends Activity {
  
  val mList = List(
	Place(0, 0, List.empty, "Mock Place 1", "", "", List.empty, 1),
	Place(0, 0, List.empty, "Mock Place 2", "", "", List.empty, 2),
	Place(0, 0, List.empty, "Mock Place 3", "", "", List.empty, 3),
	Place(0, 0, List.empty, "Mock Place 4", "", "", List.empty, 4))
  
  lazy val mAction = getActionBar
  
  override def onCreate(saved: Bundle) { // Terrible
	super.onCreate(saved)
	
	val v = new TextView(this)
    v.setText("Hello")
    setContentView(v)
    
    setupActionBar
    
    val reader = new JsonReader(new StringReader("Hello"))
    
  }
  
  private def setupActionBar {
    mAction setNavigationMode			ActionBar.NAVIGATION_MODE_TABS
    mAction setDisplayShowTitleEnabled	true
    //mAction setBackgroundDrawable		DoreWay.DECENT_GOLD
    //mAction setSplitBackgroundDrawable	DoreWay.DECENT_GOLD
    mAction setTitle					"Vanderbilt University"
  }

}