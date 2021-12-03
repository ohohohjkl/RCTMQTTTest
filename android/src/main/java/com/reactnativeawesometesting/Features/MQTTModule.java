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
//      createdClient.setCallback();
      Log.d("CREATE Client ", clientRef);
      _MQTTClients.put(clientRef, createdClient);
      promise.resolve(clientRef);
    } catch (Exception e) {
      promise.reject("Create Event Error", e);
    }
  }

  //implement connect
  @ReactMethod
  public void connect(@NonNull String clientRef) {
    try {
      Log.d("CONNECT Client ", clientRef);
      MQTTClient MQTTClient = _MQTTClients.get(clientRef);
      MQTTClient.connect();
      Log.d("CONNECT Client ", "COMPLETE");
    } catch (Exception e) {
      Log.d("Create Event Error", e.getMessage());
    }
  }

  //implement disconnect
  @ReactMethod
  public void disconnect(@NonNull String clientRef) {
    try {
      MQTTClient MQTTClient = _MQTTClients.get(clientRef);
      MQTTClient.disconnect();
    } catch (Exception e) {
    }
  }

  //implement subscribe
  @ReactMethod
  public void subscribe(@NonNull String clientRef, String topic, Integer qos) {
    try {
      MQTTClient MQTTClient = _MQTTClients.get(clientRef);
      MQTTClient.subscribe(topic, qos);
    } catch (Exception e) {
    }
  }

  //implement unsubscribe
  @ReactMethod
  public void unsubscribe(@NonNull String clientRef, @NonNull String topic) {
    try {
      MQTTClient MQTTClient = _MQTTClients.get(clientRef);
      MQTTClient.unsubscribe(topic);
    } catch (Exception e) {
    }
  }

  //implement publish
  @ReactMethod
  public void publish(@NonNull final String clientRef,
                      @NonNull final String topic,
                      @NonNull final String payload,
                      final int qos,
                      final boolean retain)
  {
    MQTTClient MQTTClient = _MQTTClients.get(clientRef);
    MQTTClient.publish(topic, payload, qos, retain);
  }

  //implement isConnected
  @ReactMethod
  public Boolean isConnected(String clientRef) {
    Log.d("MQTTModule ", clientRef + " Connected");
    MQTTClient MQTTClient = _MQTTClients.get(clientRef);
    return MQTTClient.isConnected();
  }

  //implement isSubscribed
  @ReactMethod
  public Boolean isSubscribe(String clientRef, @NonNull String topic) {
    Log.d("MQTTModule ", clientRef + " Connected");
    MQTTClient MQTTClient = _MQTTClients.get(clientRef);
    return MQTTClient.isSubbed(topic);
  }
}
