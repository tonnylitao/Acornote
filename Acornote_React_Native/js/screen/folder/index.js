'use strict';

import React, { Component, PropTypes } from 'react';
import { StyleSheet, TouchableHighlight, Text, View, Image, } from 'react-native';

import ItemsListView from './itemsListView'
import IconButton from '../../components/iconButton'

const styles = StyleSheet.create({
  view: {
    flex: 1
  },
  nav: {
    height:64, 
    paddingTop: 20,
    backgroundColor:'#373B46', 
  }
})

export default class FolderScreen extends Component {
  static propTypes = {
    navigator: PropTypes.object.isRequired,
  }

  _onPressButton = ()=>{
    this.props.navigator.pop()
  }

  render() {
    return (
      <View style={styles.view}>
        <View style={styles.nav}>
          
          <IconButton 
            onPress={this._onPressButton} 
            source={require('Acornote_React_Native/img/ic_back@3x.png')} />

        </View>
        <ItemsListView navigator={this.props.navigator}/>
      </View>
    )
  }
}
