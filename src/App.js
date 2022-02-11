// MS: 2/2/22 - Initial page based on https://reactnative.dev/docs/tutorial

import React from 'react';
import { StyleSheet, Button, View, SafeAreaView, Text, Alert} from 'react-native';

const FirstScreen = () => {
    return (
      <View style={styles.container}>
        <Text style={styles.title}>Don't Panic</Text>
        <Text style={styles.subtitle}>Everyone experiences times of anxiety in their life.</Text>
        <View style={styles.button}>
            <Text style={styles.buttonText}>Let's Get Started</Text>
          </View>
    </View>

    );
};
  

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#2D315C',

  },
  title: {
    color: 'white',
    textTransform: 'uppercase',
    textAlign: 'center',
    fontWeight: '700',
    fontSize: 40,
    fontFamily: 'sans-serif-light',
    lineHeight: 47

  },
  subtitle: {
    color: 'white',
    fontWeight: '100',
    textAlign: 'center',
    fontSize: 15,
    fontFamily: 'sans-serif',
    lineHeight: 50,
    height: 100
  
  },
  button: { 
    width: 200,
    height: 50,
    backgroundColor:'#ABA4D0',
    borderRadius: 30
  },
  buttonText: {
    textAlign: 'center',
    padding: 10,
    color: 'white',
    fontWeight: '100', 
    fontSize: 18,
    fontFamily: 'sans-serif'
  }
});

export default FirstScreen;