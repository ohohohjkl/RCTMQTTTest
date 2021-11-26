import React from 'react';
import { Button } from 'react-native';
// const { CalendarModule } = NativeModules;

type PropTypes = {
  title: string;
  onPress?: any;
}

export const NewModuleButton = (props: PropTypes) => {

  const {title, onPress} = props;
//   const onPress = () => {
//     CalendarModule.createCalendarEvent('testName22222222', 'testLocation2222222222');
//   };
  return (
    <Button
      title={title}
      color="#841584"
      onPress={onPress}
    />
  );
};
