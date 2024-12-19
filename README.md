# 🐦‍⬛ Bobolink App 🐦‍⬛

## Features

- Create a Bobolink account.
- View bird observation hotspots in the area.
- Filter hotspots based on the user's location and preferences.
- Get directions from the user's current location to a selected hotspot.
- Toggle between metric and imperial units for distance measurements.
- Save bird sightings.

## Installation

To run this app on your Android device or emulator, follow these steps:

1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Clean, rebuild, and run the app on your preferred Android device or emulator.

## Requirements

- Minimum SDK: API 27 ("Oreo"; Android 8.1) or higher.
- Allow the app to use your current location.

## How to Navigate Through the Bobolink Application

- **Get Started Window**: Contains a greeting and a button to start using the app.
- **Register/Login**: Choose between registering or logging in.
- **Find Birds Window**: Displays a bird icon with an "Open Map" button. Clicking it opens a map where you can:
  - Set the maximum distance you're willing to travel.
  - Toggle between imperial and metric units.
  - Get directions to a hotspot by selecting a marker and clicking "Get Directions."
  
At the bottom of the screen, there's a menu bar with the following options:
- **Home**: Takes you to the home screen.
- **Bird Observation**: Save a bird observation by selecting a breed and entering a name. Your current location and date are saved automatically.
- **Bird Journal**: View your bird observations.
- **Settings**: Edit your personal information or log out.

## Technologies Used

- Android Studio
- Retrofit for API communication
- Google Maps API to display the map
- eBird API for bird hotspots data
- Google Directions API to get directions to hotspots
