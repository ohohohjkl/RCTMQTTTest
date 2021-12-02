package com.reactnativeawesometesting.Models;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableMap;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MQTTOptions {
  private String host = "";
  private Integer port = 1883;
  private String protocol = "";
  private Integer protocolLevel = 3;
  private String user = "";
  private String pass = "";
  private Integer keepalive = 60;
  public String clientID = "defaultID";
  private Boolean automaticReconnect = false;
  private Boolean tls = false;
  private Boolean auth = false;

  public String uri = "";
  private MqttConnectOptions connectOptions;

  MQTTOptions(@NonNull ReadableMap params) {
    if (params.hasKey("host"))
      host = params.getString("host");
    if (params.hasKey("port"))
      port = params.getInt("port");
    if (params.hasKey("protocol"))
      protocol = params.getString("protocol");
    if (params.hasKey("keepalive"))
      keepalive = params.getInt("keepalive");
    if (params.hasKey("clientId"))
      clientID = params.getString("clientId");
    if (params.hasKey("auth"))
      auth = params.getBoolean("auth");
    if (params.hasKey("user"))
      user = params.getString("user");
    if (params.hasKey("pass"))
      pass = params.getString("pass");

    if (params.hasKey("will")) {
    }
    if (params.hasKey("willtopic")) {
    }
    if (params.hasKey("willQos")) {
    }
    if (params.hasKey("automaticReconnect"))
      automaticReconnect = params.getBoolean("automaticReconnect");

    uri = host + ":" + port;
  }

  public MqttConnectOptions getMQTTConnectOptions() {
    connectOptions = new MqttConnectOptions();

    if (protocolLevel == 3)
      connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

    connectOptions.setKeepAliveInterval(keepalive);
    connectOptions.setMaxInflight(1000);
    connectOptions.setConnectionTimeout(10);
    connectOptions.setAutomaticReconnect(automaticReconnect);

    return connectOptions;
  }

  public String getClientID() {
    String clientId = "ExampleAndroidClient";
    clientId = clientId + System.currentTimeMillis();
    return clientId;
  }

  public String getServerURI() {
    return uri;
  }
}
