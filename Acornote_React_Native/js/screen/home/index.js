import React, { Component, PropTypes } from 'react';
import {
  StyleSheet,
  TouchableHighlight,
  Text,
  View,
  Image,
} from 'react-native'

import FoldersListView from './foldersListView'
import IconButton from '../../components/iconButton'

const styles = StyleSheet.create({
  view: {
    flex: 1,
  },
  nav: {
    paddingTop: 20,
    paddingRight:5,
    paddingLeft:5,
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
          <IconButton onPress={this._onSearch} source={require('Acornote_React_Native/img/ic_search@3x.png')} />
          <Text style={styles.title}>Acornote</Text>
          <IconButton onPress={this._onAddFolder} source={require('Acornote_React_Native/img/ic_add@3x.png')} />
        </View>

        <FoldersListView 
          automaticallyAdjustContentInsets={false}
          {...this.props} />
      </View>
    )
  }
}
