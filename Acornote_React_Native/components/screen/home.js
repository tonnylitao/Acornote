import React, { Component, PropTypes } from 'react';
import {
  StyleSheet,
  TouchableHighlight,
  Text,
  View,
  Image,
} from 'react-native';

import FoldersListView from './folderslistview'
import IconButton from '../view/iconButton'

const styles = StyleSheet.create({
  view: {
    flex: 1
  },
  nav: {
    paddingTop: 20,
    height:64, 
    flexDirection: 'row',
    backgroundColor:'#000', 
    justifyContent: 'space-between',
  },
  title: {
    paddingTop:10, 
    fontSize:20, 
    fontWeight: 'bold', 
    color: '#fff'
  },
})

export default class HomeScreen extends Component {
  static propTypes = {
    navigator: PropTypes.object.isRequired,
  }

  _onSearch = ()=>{

  }

  _onAddFolder = ()=>{

  }

  render() {
    return (
      <View style={styles.view}>
        <View style={styles.nav}>
          <IconButton onPress={this._onSearch} source={require('../../img/ic_search@3x.png')} />
          <Text style={styles.title}>Acornote</Text>
          <IconButton onPress={this._onAddFolder} source={require('../../img/ic_add@3x.png')} />
        </View>

        <FoldersListView navigator={this.props.navigator}/>
      </View>
    )
  }
}
