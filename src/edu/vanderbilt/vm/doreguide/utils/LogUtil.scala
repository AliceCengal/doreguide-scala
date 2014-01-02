package edu.vanderbilt.vm.doreguide.utils

import android.util.Log

/**
 * Utility to simplify logging. Mixin this trait into any class that needs to log
 * its operation. Override the logId method so that it returns the unique id of
 * the class, usually just the class name. Use the methods provided to log.
 * 
 * Set the variable logEnabled to false for the final release APK to turn off logging.
 * 
 * On this Logcat command to get this app's logs:
 * 
 *     adb logcat DoreGuide:D
 * 
 * where D means Debug level and higher. To further filter the output, use I (Info), W
 * (Warn), and E (Error)
 */ 
trait LogUtil {
  def logId: String

  def debug(message: String) { if (LogUtil.logEnabled) Log.d("DoreGuide", logId + ": " + message) }

  def info(message: String) { if (LogUtil.logEnabled) Log.i("DoreGuide", logId + ": " + message) }

  def warn(message: String) { if (LogUtil.logEnabled) Log.i("DoreGuide", logId + ": " + message) }
 
  def error(message: String) { if (LogUtil.logEnabled) Log.e("DoreGuide", logId + ": " + message) }
}

object LogUtil {
  def logEnabled = true
}


