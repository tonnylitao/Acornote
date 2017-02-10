'use strict';

import React, { PropTypes } from 'react'
import { StyleSheet, Dimensions, TouchableHighlight, Text, View, Image, } from 'react-native'

const {
  width: screenW
} = Dimensions.get('window');

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
    maxWidth: screenW-60,
    // backgroundColor:'#0F0', 
  },
  des: {
    flex: 1,
    fontSize:15,
    // backgroundColor:'#00F', 
  },
  img: {

  }
})

const Row = ({rowData, rowID, onPress}) => {
  
  return (

    <TouchableHighlight onPress={() => {
            
          }} underlayColor="transparent">

      <View style={styles.cell}>
        <View style={styles.titleAndImg}>
          <Text style={styles.title}>{rowData}</Text>
          
          <TouchableHighlight onPress={onPress} style={styles.img}>
            <Image 
              style={{width: 44, height: 44}}
              source={{uri: 'https://images.pexels.com/photos/36764/marguerite-daisy-beautiful-beauty.jpg?h=350&auto=compress&cs=tinysrgb'}}
            />
          </TouchableHighlight>
        </View>

        <Text style={styles.des}>10 items</Text>
      </View>
    </TouchableHighlight>
  )
}

Row.propTypes = {
  rowData: React.PropTypes.object.isRequired,
  rowID: React.PropTypes.string.isRequired,
  onPress: React.PropTypes.func.isRequired,
}

export default Row