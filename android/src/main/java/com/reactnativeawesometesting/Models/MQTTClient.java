package com.reactnativeawesometesting.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.reactnativeawesometesting.Helpers.ActListenerHelper;
import com.reactnativeawesometesting.Helpers.RCTHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;

public class MQTTClient implements MqttCallbackExtended {
  private MQTTOptions options;
  private String clientRef;
  private MqttAsyncClient _client;
  private ReactApplicationContext RCTContext;
  private Map<String, Integer> topics = new HashMap<>();
  private ActListenerHelper mqttListeners;
//  private State

  public MQTTClient(@NonNull String clientRef, ReadableMap options, ReactApplicationContext CtxFrmReact) {
    this.clientRef = clientRef;
    this.options = new MQTTOptions(options);
    this.RCTContext = CtxFrmReact;
    this.mqttListeners = ActListenerHelper.getInstance(CtxFrmReact);
    this.createMQTTClient(this.options);
  }

  public void setCallback() {
    _client.setCallback(this);
  }

  private void createMQTTClient(MQTTOptions options) {
    try {
      String clientID = options.getClientID();
      String serverURI = options.getServerURI();
      MemoryPersistence memPersist = new MemoryPersistence();
      _client = new MqttAsyncClient(serverURI, clientID, memPersist);
    } catch (Exception e) {
      Log.d("CREATE Client ERRR", e.getMessage());
      e.printStackTrace();
    }
  }

  public void connect() {
    try {
      _client.connect(options.getMQTTConnectOptions(), RCTContext, mqttListeners.getConnectListener());
      this.setCallback();
    } catch (MqttException e) {
      e.printStackTrace();
      Log.d("Create Event Error", e.getMessage());
    }
  }

  public void disconnect() {
    try {
      _client.disconnect(RCTContext, mqttListeners.getDisconnectListener());
    } catch (MqttException e) {
      Log.d("Disconnect failed ...", "");
    }
  }

  public void subscribe(@NonNull final String topic, final int qos) {
    try {
      topics.put(topic, qos);
      _client.subscribe(topic, qos, RCTContext, mqttListeners.getSubListener());
    } catch (MqttException e) {
      Log.d("Cann't subscribe", "");
      e.printStackTrace();
    }
  }

  public void unsubscribe(@NonNull final String topic) {
    try {
      if (topics.containsKey(topic))
        topics.remove(topic);
      _client.unsubscribe(topic).setActionCallback(mqttListeners.getUnsubListener());
    } catch (MqttException e) {
      Log.d("", new StringBuilder("Can't unsubscribe").append(" ").append(topic).toString());
      e.printStackTrace();
    }
  }


  public void publish(@NonNull final String topic, @NonNull final String payload, final int qos,
                      final boolean retain) {
    try {
      byte[] encodedPayload = payload.getBytes("UTF-8");
      MqttMessage message = new MqttMessage(encodedPayload);
      message.setQos(qos);
      message.setRetained(retain);
      _client.publish(topic, message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void connectionLost(Throwable cause) {
    // Called when the connection to the server has been lost.
    // An application may choose to implement reconnection
    // logic at this point.
    Log.d("connectionLost","");
  }

  public void deliveryComplete(IMqttDeliveryToken token) {
  }

  public void messageArrived(@NonNull final String topic, @NonNull final MqttMessage message) throws MqttException {
    // Called when a message arrives from the server that matches any
    // subscription made by the client

    WritableMap data = Arguments.createMap();
    data.putString("topic", topic);
    data.putString("data", new String(message.getPayload()));
    data.putInt("qos", message.getQos());
    data.putBoolean("retain", message.isRetained());

    WritableMap params = Arguments.createMap();
    params.putString("event", "message");
    params.putMap("message", data);
    RCTHelper.sendEvent(RCTContext, "mqtt_events", params);
  }

  @Override
  public void connectComplete(boolean reconnect, String serverURI) {
//    Log.d("connectComplete", String.format("connectComplete. reconnect:%1$b", reconnect));
//    WritableMap data = Arguments.createMap();
//    data.putBoolean("reconnect", reconnect);
//    WritableMap params = Arguments.createMap();
//    params.putString("event", "connect");
//    params.putMap("message", data);
//    RCTHelper.sendEvent(RCTContext, "mqtt_events", params);
  }

  public boolean isConnected() {
    return _client.isConnected();
  }

  public boolean isSubbed(String topic) {
    //log("isSubbed. checking is topic: "+ topic);
    return topics.containsKey(topic);
  }
}
