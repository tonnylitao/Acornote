import React, { Component, PropTypes } from 'react'
import { Navigator, AppRegistry, StatusBar, View, } from 'react-native'

import HomeScreen from './js/screen/home'

class AcornoteApp extends Component {

  render() {
    return (
      <View style={{flex: 1}}>

        <Navigator
          style={{flex: 1}}
          initialRoute={{ component: HomeScreen }}
          renderScene={(route, navigator)=> {
            return <route.component navigator={navigator} />
          }}
        />
      </View>
    )
  }
}

AppRegistry.registerComponent('Acornote_React_Native', () => AcornoteApp);