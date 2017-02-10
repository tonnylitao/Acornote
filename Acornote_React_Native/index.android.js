import React, { Component, PropTypes } from 'react';
import {
  Navigator,
  AppRegistry,
  TouchableHighlight,
  Text,
  View,
  StatusBar,
} from 'react-native';

import HomeScreen from './components/screen/home'

class AcornoteApp extends Component {
  render() {
    return (
      <View style={{flex: 1}}>
        <StatusBar barStyle="light-content"/>
        <Navigator
          style={{flex: 1}}
          initialRoute={{ name: 'HomeScreen' }}
          renderScene={(route, navigator)=> {
            if (route.name === 'HomeScreen') {
              return <HomeScreen navigator={navigator} />
            }else if (route.name === 'FolderScreen') {
              return <HomeScreen navigator={navigator} />
            }
          }}
        />
      </View>
    )
  }
}

AppRegistry.registerComponent('Acornote_React_Native', () => AcornoteApp);