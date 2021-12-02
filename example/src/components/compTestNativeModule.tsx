import React from 'react';
import { Button } from 'react-native';
// const { CalendarModule } = NativeModules;

type PropTypes = {
  title: string;
  onPress?: any;
};

export const NewModuleButton = (props: PropTypes) => {
  const { title, onPress } = props;
  return <Button title={title} color="#841584" onPress={onPress} />;
};
