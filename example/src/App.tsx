/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect, useState} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  AppState,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import {NewModuleButton} from './components/compTestNativeModule';
import MQTTClientManager from 'react-native-awesome-testing';

var MQTTClient:any = null;

const App = () => {
  const backgroundStyle = {
    backgroundColor: Colors.lighter,
  };

  const [MQTTStatus, setMQTTStatus] = useState('');
  const [topicData, setTopicData] = useState<Array<string>>([]);

  const handleOnConnect = () => MQTTClient.connect();
  const handleOnDisconnect = () => MQTTClient.disconnect();
  const handleOnSubcribe = () => MQTTClient.subscribe('/data', 0);
  const handleOnUnsubcribe = () => MQTTClient.unsubscribe('/data');
  const handleOnPublish = () =>
    MQTTClient.publish('/data', 'testtttttttttttt', 0, false);
  const handleSetStatus = (data:any):void => {
    console.log({data});
    setMQTTStatus(data);
  };

  useEffect( () => {
    (async () => {
      MQTTClient = await MQTTClientManager.createClient({
        host: 'tcp://test.mosquitto.org',
        port: 1883,
      } as any);
      MQTTClient.on('connecting', handleSetStatus);
      MQTTClient.on('connect', handleSetStatus);
      MQTTClient.on('closed', handleSetStatus);
      MQTTClient.on('closing', handleSetStatus);
  
      MQTTClient.on('error', (data:any):void => {
        console.log({data});
        setMQTTStatus('ERROR');
      });
  
      MQTTClient.on('message', (data:object):void => {
        let temp:Array<string> = [...topicData];
        temp.push(JSON.stringify(data));
        console.log(temp);
        setTopicData([...temp]);
      });
    })()
    
  }, []); //didMount

  return (
    <SafeAreaView style={backgroundStyle}>
      <NewModuleButton title="connect" onPress={handleOnConnect} />
      <NewModuleButton title="disconnect" onPress={handleOnDisconnect} />
      <NewModuleButton title="subcribe" onPress={handleOnSubcribe} />
      <NewModuleButton title="unsubcribe" onPress={handleOnUnsubcribe} />
      <NewModuleButton title="publish" onPress={handleOnPublish} />
      <Text>{MQTTStatus}</Text>
      <View style={styles.messageBox}>
        {topicData.map((data, index) => (
          <Text key={index} style={styles.textView}>
            {data}
          </Text>
        ))}
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  textView: {
    color: 'red',
  },
  messageBox: {
    width: '100%',
    // height: 50,
    // border: '1px solid  #ddd',
    borderRadius: 4,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
