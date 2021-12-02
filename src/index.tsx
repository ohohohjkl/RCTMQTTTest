import {
  NativeModules,
  NativeEventEmitter,
  EmitterSubscription,
} from 'react-native';
import { IMqttClientOptions, MQTTEvent } from './types';
const { AwesomeTesting } = NativeModules;

interface EventHandler {
  [index: string]: any;
}
interface MQTTClientList {
  [clientRefID: string]: MQTTClient;
}

class MQTTClient {
  options: IMqttClientOptions;
  clientRef: string;
  eventHandler: EventHandler;

  constructor(options: IMqttClientOptions, clientRef: string) {
    this.options = options;
    this.clientRef = clientRef;
    this.eventHandler = {};
  }

  dispatchEventFromNative = (data: any) => {
    const { event, message } = data;
    if (!this.eventHandler[event]) return;

    this.eventHandler[event](message); //execute event
  };

  on = (eventName: MQTTEvent, callback: any) =>
    (this.eventHandler[eventName] = callback);

  connect = () => AwesomeTesting.connect(this.clientRef);

  disconnect = () => AwesomeTesting.disconnect(this.clientRef);

  subscribe = (topic: string, qos: number = 0) =>
    AwesomeTesting.subscribe(this.clientRef, topic, qos);

  unsubscribe = (topic: string = '') =>
    AwesomeTesting.unsubscribe(this.clientRef, topic);

  publish = (
    topic: string,
    data: object,
    qos: number = 0,
    retained: Boolean = false
  ) => AwesomeTesting.publish(this.clientRef, topic, data, qos, retained);

  reconnect = () => {};

  getTopics = () => {};

  removeClient = () => {};
}

class MQTTClientManager {
  clients: MQTTClientList = {};
  emitter: NativeEventEmitter;
  eventHandler: EmitterSubscription;

  constructor() {
    this.emitter = new NativeEventEmitter(AwesomeTesting);
    this.eventHandler = this.emitter.addListener(
      'mqtt_events',
      this.dispatchToClients
    );
  }

  createClient = async (options: IMqttClientOptions) => {
    let clientRef = await AwesomeTesting.createClient(options);
    console.log('clientReffffffffffff', clientRef);
    this.clients[clientRef] = new MQTTClient(options, clientRef);
    return this.clients[clientRef];
  };

  dispatchToClients = (data: object) => {
    let arrClient = Object.values(this.clients);
    arrClient.forEach((client) => client.dispatchEventFromNative(data));
  };

  removeClient = async (client: MQTTClient) => {
    let targetClient = this.clients[client.clientRef];

    await targetClient.removeClient();
    delete this.clients[client.clientRef]; //removeClient From MQTT manager

    this.eventHandler.remove();
    this.eventHandler = {} as EmitterSubscription;
  };

  disconnectAll = () => AwesomeTesting.disconnectAll();
}

export default new MQTTClientManager();
