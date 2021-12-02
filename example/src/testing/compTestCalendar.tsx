import React, { useEffect } from 'react';
import { Button, NativeModules } from 'react-native';
const { CalendarModule } = NativeModules;

type PropTypes = {
  title: string;
};

export const TestNativeModule = (props: PropTypes) => {
  const onPress = () => {
    CalendarModule.createCalendarEvent(
      'testName22222222',
      'testLocation2222222222'
    );
  };

  useEffect(() => {
    (async () => {
      let clientID = await CalendarModule.createCalendarEventAsync(
        'testName22222222',
        'testLocation2222222222'
      );
      console.log('clientID', clientID);
    })();
  });
  return <Button title={'title'} color="#841584" onPress={onPress} />;
};
