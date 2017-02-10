'use strict';

import React, { Component, PropTypes } from 'react'
import { StyleSheet, ListView, TouchableHighlight, Text, View, Image, } from 'react-native'

import realm from 'Acornote_React_Native/js/realm'

import FolderScreen from 'Acornote_React_Native/js/screen/folder'
import FolderRow from './folderRow'

export default class FoldersListView extends Component {
  
  constructor(props) {
    super(props);

    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1.title !== r2.title});

    this.state = {
      dataSource: ds.cloneWithRows(realm.objects('Folder'))
    };
  }

  _renderRow = (rowData, _, rowID)=>{
    return (
      <FolderRow 
        rowData={rowData} 
        rowID={rowID} 
        onPress={(rowID)=>{
            this.props.navigator.push({
              component: FolderScreen
            })
          }}
      />
    )
  }

  render() {
    return (
      <View style={{backgroundColor:'#F1F0EF', flex: 1}}>
        <ListView
          style={{backgroundColor:'#F1F0EF'}}
          dataSource={this.state.dataSource}
          renderRow={this._renderRow}
        />
      </View>
    );
  }
}