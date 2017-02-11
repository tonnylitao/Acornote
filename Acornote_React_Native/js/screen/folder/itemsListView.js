'use strict';

import React, { Component, PropTypes } from 'react'
import { StyleSheet, ListView, TouchableHighlight, Text, View, Image,} from 'react-native'

import ItemRow from './itemRow'

const styles = StyleSheet.create({
  view: {
    flex: 1,
  },
  listview: {
    backgroundColor: 'white' //for andorid
  },
  separator: {
    flex: 1,
    marginLeft: 10,
    height: StyleSheet.hairlineWidth,
    backgroundColor: '#8E8E8E',
  },
})

export default class FolderListView extends Component {
  
  constructor(props) {
    super(props);

    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});

    this.state = {
      dataSource: ds.cloneWithRows([
        '11111111111111111111111111111111111111111111', 'Joel', 'James', 'Jimmy', 'Jackson', 'Jillian', 'Julie', 'Devin'
      ])
    };
  }

  _renderRow = (rowData, _, rowID)=>{
    return (
      <ItemRow 
        rowData={rowData} 
        rowID={rowID} 
        onPress={(rowID)=>{
            
          }}
      />
    )
  }

  render() {
    return (
      <View style={styles.view}>
        <ListView
          style={styles.listview}
          dataSource={this.state.dataSource}
          renderRow={this._renderRow}
          renderSeparator={(sectionId, rowId) => <View key={rowId} style={styles.separator} />}
        />
      </View>
    );
  }
}