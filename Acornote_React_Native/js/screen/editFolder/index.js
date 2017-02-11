'use strict';

import React, { Component } from 'react';
import { StyleSheet, Modal, KeyboardAvoidingView, TextInput, View, } from 'react-native';

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
          >
          <KeyboardAvoidingView behavior='height' style={styles.view}>
         
            <View style={styles.buttons}>
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
              onChangeText={(text) => this.setState({text})}
            />

            <View style={styles.buttons}>
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
    backgroundColor: 'red',
    justifyContent: 'space-between',
    alignItems: 'stretch',
  },
  textInput: {
    color: 'white',
    textAlign: 'center',
    fontSize: 50,
    marginRight: 10,
    marginLeft: 10,
  },
  buttons: {
    margin: 10,
    height: 44,
    flexDirection: 'row',
    justifyContent: 'space-between',
  }
})