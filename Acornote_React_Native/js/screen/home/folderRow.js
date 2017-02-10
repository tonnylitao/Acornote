'use strict';

import React, { PropTypes } from 'react'
import { StyleSheet, TouchableHighlight, Text, View, } from 'react-native'

const cardRadius = 3

const styles = StyleSheet.create({
  cell: {
    marginLeft:18,
    marginRight:18, 
    marginTop: 10,
    marginBottom: 10,
    height: 70, 
    borderRadius: cardRadius,
    backgroundColor:'white', 
    flexDirection: 'row', 
    alignItems: 'center',
  },
  folderColor: {
    height: 70, 
    width: 5,
    borderTopLeftRadius: cardRadius,
    borderBottomLeftRadius: cardRadius,
    backgroundColor:'red', 
  },
  textContainer: {
    paddingLeft: 5,
    flexDirection: 'column'
  },
  title: {
    fontSize:20,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  des: {
    fontSize:15,
  },
  shadow: {
    marginTop: -10,
  }
})

const Row = ({rowData, rowID, onPress}) => {
  
  return (

    <TouchableHighlight onPress={onPress} underlayColor="transparent">

      <View style={styles.cell} shadowColor='black' shadowRadius={3} shadowOffset={{width:0, height: 4}} shadowOpacity={0.05}>
        
        <View style={styles.folderColor} />
        
        <View style={styles.textContainer}>
          
          <Text style={styles.title} numberOfLines={2}>{rowData.title}</Text>
          
          <Text style={styles.des}>{rowData.items.length} items</Text>
        
        </View>

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