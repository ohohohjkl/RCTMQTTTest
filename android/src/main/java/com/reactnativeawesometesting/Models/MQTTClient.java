package com.reactnativeawesometesting.Models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.reactnativeawesometesting.Helpers.RCTHelper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTClient implements MqttCallbackExtended {
  private MQTTOptions options;
  private String clientRef;
  private MqttAsyncClient _client;
  private ReactApplicationContext RCTContext;

  public MQTTClient(@NonNull String clientRef, ReadableMap options, ReactApplicationContext CtxFrmReact){
      this.clientRef = clientRef;
      this.options = new MQTTOptions(options);
      this.RCTContext = CtxFrmReact;
      this.createMQTTClient(this.options);
  }

  private void createMQTTClient(MQTTOptions options) {
    try {
      String clientID = options.getClientID();
      String serverURI = options.getServerURI();
      MemoryPersistence memPersist = new MemoryPersistence();
      Log.d("B4CREATE","test");
      _client = new MqttAsyncClient(serverURI, clientID, memPersist);
      Log.d("AFTERCREATE","test");
    } catch (MqttException e) {
      Log.d("CREATE Client ERRR", e.getMessage());
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      Log.d("IllegalArgException", e.getMessage());
      e.printStackTrace();
    }
    catch (NullPointerException e) {
      Log.d("NullPointerException", e.getMessage());
      e.printStackTrace();
    }
  }

  public void connect(){
    try{
      _client.connect(options.getMQTTConnectOptions(), RCTContext, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          WritableMap params = Arguments.createMap();
          params.putString("eventProperty", "someValue");
          RCTHelper.sendEvent(RCTContext,  "mqtt_events", params);
          //resubscribe
        }
        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          WritableMap params = Arguments.createMap();
          params.putString("event", "error");
          params.putString("message", "connection failure " +exception.getMessage());
          RCTHelper.sendEvent(RCTContext,  "mqtt_events", params);
        }
      });
    }catch (MqttException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void connectComplete(boolean reconnect, String serverURI) {

  }

  @Override
  public void connectionLost(Throwable cause) {

  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {

  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {

  }

}
