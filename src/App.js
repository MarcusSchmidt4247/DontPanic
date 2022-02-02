// MS: 2/2/22 - Initial page based on https://reactnative.dev/docs/tutorial

import React from 'react';
import { View, Text } from 'react-native';

const MainScreen: () => Node = () => {
    return (
        <View style={{flex: 1, justifyContent: "center", alignItems: "center"}}>
            <Text>This is a placeholder for the main screen.</Text>
        </View>
    );
}

export default MainScreen;