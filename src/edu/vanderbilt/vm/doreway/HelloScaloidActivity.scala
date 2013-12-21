package edu.vanderbilt.vm.doreway

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import com.google.gson.stream.JsonReader
import java.io.StringReader
import android.widget.TextView
import android.app.ActionBar

class HelloScaloidActivity extends Activity {

  val mList = List("Hello", "World")

  override def onCreate(saved: Bundle) {
	super.onCreate(saved)
	
	val v = new TextView(this)
    v.setText("Hello")
    setContentView(v)
    
    setupActionBar
    
    val reader = new JsonReader(new StringReader("Hello"))
    
  }
  
  private def setupActionBar {
    val mAction = getActionBar
    mAction setNavigationMode			ActionBar.NAVIGATION_MODE_TABS
    mAction setDisplayShowTitleEnabled	true
    //mAction setBackgroundDrawable		DoreWay.DECENT_GOLD
    //mAction setSplitBackgroundDrawable	DoreWay.DECENT_GOLD
    mAction setTitle					"Vanderbilt University"
  }

}
