import React, { Component, PropTypes } from 'react';
import {
  StyleSheet,
  TouchableHighlight,
  Image,
} from 'react-native';

const styles = StyleSheet.create({
  iconButton: {
    width: 44,
    height: 44,
    alignItems: 'center',
    justifyContent: 'center',
  }
})

const IconButton = ({onPress, source})=>{
  return (
  	<TouchableHighlight onPress={onPress} style={styles.iconButton}>
	    <Image
	      source={source}
	    />
	  </TouchableHighlight>
  )
}

IconButton.propTypes = {
	onPress: React.PropTypes.func.isRequired,
	source: React.PropTypes.number.isRequired,
}

export default IconButton