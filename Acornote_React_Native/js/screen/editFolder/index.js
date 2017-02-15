'use strict';

import React, { Component } from 'react';
import { StyleSheet, Modal, KeyboardAvoidingView, TextInput, View, StatusBar, } from 'react-native';

import IconButton from 'Acornote_React_Native/js/components/iconButton'

export default class EditFolderScreen extends Component {

  state = {
    text: ''
  }

  _onSure = ()=>{

  }
  _onEditLink = ()=>{

  }
  _onEditMark = ()=>{
    
  }
  _onEditFlip = ()=>{
    
  }
  _onEditAudio = ()=>{
    
  }

  render() {
    return (
      <Modal
          animationType={"slide"}
          transparent={false}
          visible={this.props.modalVisible}
          onRequestClose={()=>{}}
          >
          <StatusBar 
            hidden={true}
            animated={true} 
            showHideTransition="slide"
          />
          <KeyboardAvoidingView behavior='height' style={styles.view} >
         
            <View style={styles.canselSureBtns}>
              <IconButton 
                onPress={this.props.onCancel} 
                source={require('Acornote_React_Native/img/ic_cancel@3x.png')} />


              <IconButton 
                onPress={this._onSure} 
                source={require('Acornote_React_Native/img/ic_ok@3x.png')} />
            </View>

            <TextInput
              ref='title'
              style={styles.textInput}
              placeholder="Folder name"
              autoFocus={true}
              autoCorrect={true}
              onChangeText={(text) => this.setState({text})}
              clearButtonMode="while-editing"
              returnKeyType="done"
              enablesReturnKeyAutomatically={true}
              underlineColorAndroid="transparent"
            />

            <View style={styles.bottomBtns}>
              <IconButton 
                onPress={this._onEditLink} 
                source={require('Acornote_React_Native/img/ic_link_off@3x.png')} />

              <IconButton 
                onPress={this._onEditMark} 
                source={require('Acornote_React_Native/img/ic_mark_off@3x.png')} />

              <IconButton 
                onPress={this._onEditFlip} 
                source={require('Acornote_React_Native/img/ic_flip_off@3x.png')} />

              <IconButton 
                onPress={this._onEditAudio} 
                source={require('Acornote_React_Native/img/ic_audio_off@3x.png')} />
            </View>

         </KeyboardAvoidingView>
        </Modal>
    );
  }
}

const styles = StyleSheet.create({
  view: {
    flex: 1,
    flexDirection:'column',
    backgroundColor: '#19D2B9',
    justifyContent: 'space-between',
    alignItems: 'stretch',
  },
  textInput: {
    flex: 1,
    color: 'white',
    textAlign: 'center',
    fontSize: 50,
    marginRight: 10,
    marginLeft: 10,
  },
  canselSureBtns: {
    margin: 10,
    height: 44,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  bottomBtns: {
    margin: 10,
    height: 44,
    flexDirection: 'row',
    justifyContent: 'space-between'
  }
})