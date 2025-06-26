# Spotify Authorization Setup Guide

This guide explains how to set up Spotify authorization for the test suite. This is a one time step needed to obtain the Refresh Access Token which is subsequently used by the Test Suite.

## Prerequisites

1. A Spotify Developer account
2. A Spotify application registered in the Spotify Developer Dashboard

## Setup Steps

### 1. Create a Spotify Application

1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Click "Create App"
3. Fill in the required information:
    - App name: `Spotify Test Suite` (or any name you prefer)
    - App description: `Test suite for Spotify API`
    - Redirect URI: `http://127.0.0.1:8888/callback`
4. Accept the terms and create the app

### 2. Get Your Credentials

After creating the app, you'll get:
- **Client ID**: A 32-character string
- **Client Secret**: A 32-character string

### 3. Configure Environment Variables

Set the following environment variables:

```bash
export SPOTIFY_CLIENT_ID="your_client_id_here"
export SPOTIFY_CLIENT_SECRET="your_client_secret_here"
```

## Installation

This runs on Node.js. On [its website](http://www.nodejs.org/download/) you can find instructions on how to install it.

Once npm is installed check with 
```bash
npm -v 
```

Install the app dependencies running:

```bash
npm install
```

## Running 

From a console shell:

```bash
npm start
```

Then, open `http://127.0.0.1:8888` in a browser.

<img width="427" alt="Screenshot 2025-06-26 at 5 05 28 AM" src="https://github.com/user-attachments/assets/11215bc8-8656-4da0-b734-18c0da8f9dd1" />

Post clicking on `Log in with Spotify`

Spotify OAuth Page should open up post logging in you should see a page like below

<img width="941" alt="Screenshot 2025-06-26 at 5 27 08 AM" src="https://github.com/user-attachments/assets/fd996b70-6fe4-4ebb-8421-7df2f059f255" />

Copy the entire URL and extract the refresh_token from the url `http://127.0.0.1:8888/#access_token=<SPOTIFY_ACCESS_TOKEN>&refresh_token=<SPOTIFY_REFRESH_TOKEN>`


### Authorization Code Flow
```bash
export SPOTIFY_CLIENT_ID="your_client_id"
export SPOTIFY_CLIENT_SECRET="your_client_secret"
export SPOTIFY_REFRESH_TOKEN="your_authorization_code"
mvn test
```

## Troubleshooting

### Common Issues

1. **"SPOTIFY_CLIENT_ID and SPOTIFY_CLIENT_SECRET environment variables must be set"**
    - Make sure you've set both environment variables correctly
    - Check that there are no extra spaces or quotes

2. **"Using localhost in the redirect uri instead of 127.0.0.1"**
    - This behavior is due to recent change in Spotify's security policy for http routes

## Security Notes

- Never commit your client secret or access tokens to version control
- Use environment variables or secure configuration management
- Access tokens expire after 1 hour - you may need to refresh them

## API Scopes

The test suite requires the following scopes:
- `user-library-read`
- `user-library-modify`
- `playlist-modify-public`
- `playlist-modify-private`
- `playlist-read-private`

These scopes are automatically requested during the authorization process.


