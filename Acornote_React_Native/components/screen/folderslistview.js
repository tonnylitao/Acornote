import React, { Component, PropTypes } from 'react';
import {
  StyleSheet,
  ListView,
  TouchableHighlight,
  Text,
  View,
} from 'react-native';

import FolderScreen from './folder'

const styles = StyleSheet.create({
  cell: {
    marginLeft:18,
    marginRight:18, 
    marginTop: 10,
    marginBottom: 10,
    height: 70, 
    borderRadius: 3, 
    overflow:'hidden', 
    backgroundColor:'white', 
    flexDirection: 'row', 
    alignItems: 'center'
  },
  folderColor: {
    height: 70, 
    width: 5,
    backgroundColor:'#000', 
  },
  textContainer: {
    flexDirection: 'column'
  },
  title: {
    fontSize:20,
    backgroundColor:'#0F0', 
  },
  des: {
    fontSize:15,
    backgroundColor:'#00F', 
  }
})

export default class FoldersListView extends Component {
  
  constructor(props) {
    super(props);

    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});

    this.state = {
      dataSource: ds.cloneWithRows([
        'John', 'Joel', 'James', 'Jimmy', 'Jackson', 'Jillian', 'Julie', 'Devin'
      ])
    };
  }

  _renderRow = (rowData, sectionID, rowID) => 
    <TouchableHighlight onPress={() => {
            this.props.navigator.push({
              component: FolderScreen
            })
          }} underlayColor="transparent">

      <View style={styles.cell}>
        <View style={styles.folderColor} />
        <View style={styles.textContainer}>
          <Text style={styles.title} numberOfLines={2}>{rowData}</Text>
          <Text style={styles.des}>10 items</Text>
        </View>
      </View>
    </TouchableHighlight>

  render() {
    return (
      <View style={{backgroundColor:'#F1F0EF'}}>
        <ListView
          style={{backgroundColor:'#F1F0EF'}}
          dataSource={this.state.dataSource}
          renderRow={this._renderRow}
        />
      </View>
    );
  }
}