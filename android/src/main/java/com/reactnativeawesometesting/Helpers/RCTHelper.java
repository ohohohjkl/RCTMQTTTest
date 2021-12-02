package com.reactnativeawesometesting.Helpers;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.lang.*;
import java.util.*;

public class RCTHelper {
  public static void sendEvent(
    ReactContext RCTContext,
    String eventName,
    @Nullable WritableMap params) {
    RCTContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }
}
