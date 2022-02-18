// MS: 2/2/22 - Initial page based on https://reactnative.dev/docs/tutorial

import React from 'react';
import { StyleSheet, Button, View, Text} from 'react-native';

const HomeScreen = () => {
    return (
      <View style={styles.container}>
        <Text style={styles.title}>Hello, there!</Text>
        <Text style={styles.subtitle}>Thought of the day</Text>
        <View style={styles.thought}>
          <Text style={styles.thoughtText}>The building blocks of wellbeing</Text>
        </View>
        <View style={styles.read}>
          <Text style={styles.readText}>Read more</Text>
        </View>
        <View style={styles.module}>
          <Text style={styles.moduleText}>Modules</Text>
        </View>
        <View style={styles.guided}>
          <Text style={styles.guidedText}>Guided Breathing</Text>
        </View>
         <View style={styles.self}>
          <Text style={styles.selfText}>Self-Reflection</Text>
        </View>
        <View style={styles.mental}>
          <Text style={styles.mentalText}>Mental Excercises</Text>
        </View>
        <View style={styles.app}>
          <Text style={styles.appText}>App Excercises</Text>
        </View>
    </View>
    );
};
  

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#2D315C'

  },
  title: {
    color: 'white',
    textTransform: 'uppercase',
    justifyContent: 'left',
    alignItems: 'left',
    fontWeight: '700',
    fontSize: 35,
    fontFamily: 'sans-serif-light',
    marginLeft: 20,
    lineHeight: 250

  },
  subtitle: {
    color: 'white',
    textTransform: 'uppercase',
    justifyContent: 'left',
    alignItems: 'left',
    fontWeight: '700',
    fontSize: 20,
    fontFamily: 'sans-serif-light',
    marginLeft: 20,
    top: -90

  },
  thought: { 
    width: 350,
    height: 200,
    marginLeft: 20,
    top: -70,
    backgroundColor:'#ABA4D0',
    borderRadius: 30
  },
  thoughtText: {
    textAlign: 'left',
    padding: 42,
    color: 'white',
    fontWeight: '100', 
    fontSize: 25,
    fontFamily: 'sans-serif'
  },
  read: { 
    width: 95,
    height: 40,
    marginLeft: 20,
    top: -130,
    backgroundColor:'black',
    borderRadius: 30,
    left: 210,
    zIndex: 4
  },
  readText: {
    textAlign: 'left',
    color: 'white',
    fontWeight: '100', 
    fontSize: 14,
    top: 10,
    left: 14,
    fontFamily: 'sans-serif',
    zIndex: 4
  },
  module: { 
    width: 170,
    height: 80,
    marginLeft: 20,
    top: -70,
    backgroundColor:'#ABA4D0',
    borderRadius: 30
  },
  moduleText: {
    textAlign: 'center',
    padding: 31,
    color: 'white',
    fontWeight: '100', 
    fontSize: 15,
    fontFamily: 'sans-serif'
  },
  mental: { 
    width: 170,
    height: 80,
    marginLeft: 20,
    top: -220,
    backgroundColor:'#ABA4D0',
    borderRadius: 30
  },
  mentalText: {
    textAlign: 'center',
    padding: 31,
    color: 'white',
    fontWeight: '100', 
    fontSize: 15,
    fontFamily: 'sans-serif'
  },
  app: { 
    width: 170,
    height: 80,
    marginLeft: 20,
    top: -210,
    backgroundColor:'#ABA4D0',
    borderRadius: 30
  },
  appText: {
    textAlign: 'center',
    padding: 30,
    color: 'white',
    fontWeight: '100', 
    fontSize: 15,
    fontFamily: 'sans-serif'
  },
  guided: { 
    width: 170,
    height: 80,
    marginLeft: 20,
    left: 180,
    top: -150,
    backgroundColor:'#ABA4D0',
    borderRadius: 30
  },
  guidedText: {
    textAlign: 'center',
    padding: 30,
    color: 'white',
    fontWeight: '100', 
    fontSize: 15,
    fontFamily: 'sans-serif'
  },
  self: { 
    width: 170,
    height: 80,
    marginLeft: 20,
    left: 180,
    top: -140,
    backgroundColor:'#ABA4D0',
    borderRadius: 30
  },
  selfText: {
    textAlign: 'center',
    padding: 30,
    color: 'white',
    fontWeight: '100', 
    fontSize: 15,
    fontFamily: 'sans-serif'
  }
});

export default HomeScreen;