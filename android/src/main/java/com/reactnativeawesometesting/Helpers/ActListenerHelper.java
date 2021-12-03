package com.reactnativeawesometesting.Helpers;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/*
@implement Listener Singleton
 */
public class ActListenerHelper {
  private static ActListenerHelper instance;

  IMqttActionListener disconnectListener;
  IMqttActionListener connectListener;
  IMqttActionListener subListener;
  IMqttActionListener unsubListener;

  private ActListenerHelper(ReactApplicationContext RCTContext) {
    this.initDisconnectListener(RCTContext);
    this.initConnectListener(RCTContext);
    this.initSubListener(RCTContext);
    this.initUnsubListener(RCTContext);
  }

  public static ActListenerHelper getInstance(ReactApplicationContext RCTContext) {
    if (instance == null) {
      instance = new ActListenerHelper(RCTContext);
    }
    return instance;
  }

  private void initDisconnectListener(ReactApplicationContext RCTContext) {
    disconnectListener = new IMqttActionListener() {
      public void onSuccess(IMqttToken asyncActionToken) {
        Log.d("Disconnect Completed", "");
        WritableMap params = Arguments.createMap();
        params.putString("event", "closed");
        params.putString("message", "Disconnect");
        RCTHelper.sendEvent(RCTContext, "mqtt_events", params);
      }

      public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.d("TEST", new StringBuilder("Disconnect failed").append(exception).toString());
      }
    };
  }

  private void initConnectListener(ReactApplicationContext RCTContext) {
    connectListener = new IMqttActionListener() {
      @Override
      public void onSuccess(IMqttToken asyncActionToken) {
         WritableMap params = Arguments.createMap();
         params.putString("event", "connect");
         params.putString("message", "connected");
         RCTHelper.sendEvent(RCTContext, "mqtt_events", params);
        //resubscribe
      }

      @Override
      public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        WritableMap params = Arguments.createMap();
        params.putString("event", "error");
        params.putString("message", "connection failure " + exception.getMessage());
        RCTHelper.sendEvent(RCTContext, "mqtt_events", params);
      }
    };
  }

  private void initSubListener(ReactApplicationContext RCTContext) {
    subListener = new IMqttActionListener() {
      @Override
      public void onSuccess(IMqttToken asyncActionToken) {
        // The message was published
        Log.d("Subscribe success", "ok");
      }

      @Override
      public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        // The subscription could not be performed, maybe the user was not
        // authorized to subscribe on the specified topic e.g. using wildcards
        Log.d("Subscribe failed", "");
      }
    };
  }

  private void initUnsubListener(ReactApplicationContext RCTContext) {
    unsubListener = new IMqttActionListener() {
      @Override
      public void onSuccess(IMqttToken asyncActionToken) {

      }

      @Override
      public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

      }
    };
  }

  public IMqttActionListener getDisconnectListener() {
    return disconnectListener;
  }

  public IMqttActionListener getConnectListener() {
    return connectListener;
  }

  public IMqttActionListener getSubListener() {
    return subListener;
  }

  public IMqttActionListener getUnsubListener() {
    return unsubListener;
  }
}
