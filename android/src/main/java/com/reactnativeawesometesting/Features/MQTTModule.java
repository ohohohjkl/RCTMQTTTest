package com.reactnativeawesometesting.Features;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativeawesometesting.Models.MQTTClient;

import java.util.UUID;
import java.util.Hashtable;

public class MQTTModule extends ReactContextBaseJavaModule {
  private Hashtable<String, MQTTClient> _MQTTClients;
  private ReactApplicationContext _RCTContext;

  public MQTTModule(ReactApplicationContext context) {
    super(context);
    _RCTContext = context;
    _MQTTClients = new Hashtable<String, MQTTClient>();
  }

  public String getName() {
    return "AwesomeTesting";
  }

  //implement createClient
  @ReactMethod
  public void createClient(@NonNull ReadableMap options, Promise promise) {
    try {
      String clientRef = UUID.randomUUID().toString();
      MQTTClient createdClient = new MQTTClient(clientRef, options, _RCTContext);
      Log.d("CREATE Client ", clientRef );
      _MQTTClients.put(clientRef,createdClient );
      promise.resolve(clientRef);
    } catch(Exception e) {
      promise.reject("Create Event Error", e);
    }
  }

  //implement connect
  @ReactMethod
  public void connect(@NonNull  String clientRef) {
    try {
      MQTTClient MQTTClient = _MQTTClients.get(clientRef);
      MQTTClient.connect();
    } catch(Exception e) {
    }
  }
  //implement disconnect
  //implement subscribe
  //implement unsubscribe
  //implement publish

  //implement isConnected
  @ReactMethod
  public Object isConnected(String clientRef) {
    Log.d("MQTTModule ", clientRef + " Connected");
    return _MQTTClients.get(clientRef);
  }
  //implement isSubscribed
  @ReactMethod
  public void isSubscribe(String clientRef) {
    Log.d("CalendarModule", clientRef + " Subscribed");
  }

}
