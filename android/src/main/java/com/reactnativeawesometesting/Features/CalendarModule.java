package com.reactnativeawesometesting.Features;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;

public class CalendarModule extends ReactContextBaseJavaModule {
  public CalendarModule(ReactApplicationContext context) {
    super(context);
  }

  // add to CalendarModule.java
  @Override
  public String getName() {
    return "CalendarModule";
  }

  @ReactMethod
  public void createCalendarEvent(String name, String location) {
    Log.d("CalendarModule", "Create event called with name: " + name
      + " and location: " + location);
  }

  @ReactMethod
  public void createCalendarEventAsync(String name, String location, Promise promise) {
    try {
      Integer eventId = 222222222;
      promise.resolve(eventId);
    } catch(Exception e) {
      promise.reject("Create Event Error", e);
    }
  }

}
