import React, { Component, PropTypes } from 'react';
import {
  NavigatorIOS,
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
        <NavigatorIOS
          style={{flex: 1}}
          navigationBarHidden={true}
          initialRoute={{
            component: HomeScreen,
            title: 'Acornote'
          }}
          renderScene={(route, navigator)=> {
            if (route.name === 'HomeScreen') {
              return <HomeScreen navigator={navigator} />
            }else if (route.name === 'FolderScreen') {
              return <FolderScreen navigator={navigator} />
            }
          }}
        />
      </View>
    )
  }
}

AppRegistry.registerComponent('Acornote_React_Native', () => AcornoteApp);
