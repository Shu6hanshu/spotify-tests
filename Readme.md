# Spotify Smoke Test Plan

## Authorization Setup

Before running the tests, you need to set up Spotify authorization. Please refer to [Spotify_Authorization_Setup.md](authorization_code/Spotify%20Authorization%20Setup.md) for detailed instructions.

### Quick Setup

1. Create a Spotify application in the [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Set environment variables:
   ```bash
   export SPOTIFY_CLIENT_ID="your_client_id"
   export SPOTIFY_CLIENT_SECRET="your_client_secret"
   export SPOTIFY_REFRESH_TOKEN="your_refresh_token"
   ```
3. Run the tests:
   ```bash
   mvn clean compile 
   mvn test
   ```
4. Look at the results:
   Open [Test Report](target/surefire-reports/index.html) in a browser



## Documentation
- [Spotify Web API Reference](https://developer.spotify.com/documentation/web-api/reference/)

/*
Step 0: Search for the following songs to obtain their Spotify track IDs:
A - Track: Zinda ; Album: Bhaag Milkha Bhaag
B - Track: Give me some Sunshine ; Album: 3 Idiots
C - Track: In The End ; Artist: Linkin Park
D - Track: Yun Hi Chala Chal ; Album: Swades
E - Track: Khalasi
F - Track: Kya Jaipur Kya Dilli ; Artist: Rahgir
G - Track: Senorita ; Album: Zindagi Na Milegi Dobara

  - Endpoint: GET /v1/search
  - Query: q=<track name> [artist:<artist>] [album:<album>], type=track
  - For each song, search using the track name and, if available, album or artist for better accuracy.
  - Example: 
      - For "Zinda" from album "Bhaag Milkha Bhaag":
        GET /v1/search?q=track:Zinda%20album:Bhaag%20Milkha%20Bhaag&type=track
      - For "In The End" by Linkin Park:
        GET /v1/search?q=track:In%20The%20End%20artist:Linkin%20Park&type=track
  - Extract the track ID from the first result of each search.
  - Use these track IDs in subsequent playlist operations.
*/
/*
Prerequisites:
   - Obtain a valid OAuth access token with the required scopes:
     - playlist-modify-public
     - playlist-modify-private
     - playlist-read-private
   - Have track IDs for tracks A, B, C, D, E, F, G (replace with real Spotify track IDs).
*/
/*
Test Steps:

Step 1: Create a playlist named "Road Trip Shimla"
  - Endpoint: POST /v1/users/{user_id}/playlists
  - Body: { "name": "Road Trip Shimla", "public": false }
  - Assert: Playlist is created and name matches.

Step 2: Add tracks A, B, C to the created playlist
  - Endpoint: POST /v1/playlists/{playlist_id}/tracks
  - Body: { "uris": ["spotify:track:A", "spotify:track:B", "spotify:track:C"] }
  - Assert: Tracks are added (check snapshot or GET playlist tracks).

Step 3: Replace track C with D in the playlist
  - Endpoint: PUT /v1/playlists/{playlist_id}/tracks
  - Body: { "uris": ["spotify:track:A", "spotify:track:B", "spotify:track:D"] }
  - Assert: Playlist now contains A, B, D.

Step 4: Create another playlist named "Road Trip Jaipur"
  - Endpoint: POST /v1/users/{user_id}/playlists
  - Body: { "name": "Road Trip Jaipur", "public": false }
  - Assert: Playlist is created and name matches.

Step 5: Add tracks E, F, G to "Road Trip Jaipur"
  - Endpoint: POST /v1/playlists/{playlist_id}/tracks
  - Body: { "uris": ["spotify:track:E", "spotify:track:F", "spotify:track:G"] }
  - Assert: Tracks are added (check snapshot or GET playlist tracks).
*/
/*
Cleanup (optional):
  - Delete the created playlists to keep the account clean.
*/
/*
Notes:
  - All API calls must include Authorization: Bearer <access_token> header.
  - Use GET /v1/me to get the current user's user_id.
  - Use GET /v1/playlists/{playlist_id}/tracks to verify playlist contents.
  - Replace A, B, C, D, E, F, G with actual Spotify track IDs for real tests.
*/
/*
APIs being smoke tested:
- GET /v1/search (to find track IDs for the given songs)
- GET /v1/me (to get current user ID)
- POST /v1/users/{user_id}/playlists (to create playlists)
- POST /v1/playlists/{playlist_id}/tracks (to add tracks to playlists)
- PUT /v1/playlists/{playlist_id}/tracks (to replace tracks in a playlist)
- GET /v1/playlists/{playlist_id}/tracks (to verify playlist contents)
- DELETE /v1/playlists/{playlist_id}/followers (to delete playlists for cleanup)
*/
