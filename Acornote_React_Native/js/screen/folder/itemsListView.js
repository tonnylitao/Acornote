import React, { Component, PropTypes } from 'react'
import {
  StyleSheet,
  ListView,
  TouchableHighlight,
  Text,
  View,
  Image,
} from 'react-native'


const styles = StyleSheet.create({
  cell: {
    marginTop: 10,
    marginLeft: 10,
    marginRight: 10,
    overflow:'hidden', 
    backgroundColor:'white', 
    flexDirection: 'column', 
  },
  
  titleAndImg: {
    flex: 1,
    flexDirection: 'row', 
    justifyContent: 'space-between',
  },
  title: {
    fontSize:20,
    // backgroundColor:'#0F0', 
  },
  des: {
    flex: 1,
    fontSize:15,
    // backgroundColor:'#00F', 
  },
  img: {

  },
  separator: {
    backgroundColor:'gray',
    marginLeft: 10,
    height: StyleSheet.hairlineWidth,
  }
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

  _renderRow = (rowData, sectionID, rowID) => 
    <TouchableHighlight onPress={() => {
            
          }} underlayColor="transparent">

      <View style={styles.cell}>
        <View style={styles.titleAndImg}>
          <Text style={styles.title}>{rowData}</Text>
          
          <TouchableHighlight onPress={this._onPressButton} style={styles.img}>
            <Image 
              style={{width: 44, height: 44}}
              source={{uri: 'https://images.pexels.com/photos/36764/marguerite-daisy-beautiful-beauty.jpg?h=350&auto=compress&cs=tinysrgb'}}
            />
          </TouchableHighlight>
        </View>

        <Text style={styles.des}>10 items</Text>
      </View>
    </TouchableHighlight>

  render() {
    return (
      <View>
        <ListView
          dataSource={this.state.dataSource}
          renderRow={this._renderRow}
          renderSeparator={(sectionId, rowId) => <View key={rowId} style={styles.separator} />}
        />
      </View>
    );
  }
}