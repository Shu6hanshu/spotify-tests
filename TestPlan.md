# Spotify Project Test Plan

## 1. Overview
This document outlines the test plan for both the **Frontend (Spotify Public Web Player UI)** and **Backend (API Smoke Tests)** of the Spotify integration project. It covers objectives, scope, test scenarios, data management, and extensibility guidelines.

---

## 2. Objectives
- **Frontend:** Verify the Spotify public website homepage loads correctly and that search functionality works for specific songs.
- **Backend:** Validate core Spotify API flows (playlist creation, track management, user profile) via automated smoke tests.

---

## 3. Scope
- **Frontend:**
  - Navigate to the Spotify public website (https://open.spotify.com/)
  - Verify visibility of key homepage elements (logo, search bar, navigation menu, etc.)
  - Use the search bar to search for specific songs (e.g., "Zinda", "Give me some Sunshine") and verify results
- **Backend:**
  - Track search
  - Playlist creation and management
  - Adding/replacing tracks in playlists
  - Cleanup (playlist deletion)

---

## 4. Tech Stack
- **Frontend:**
  - HTML, JavaScript, jQuery, Handlebars.js, Bootstrap
  - Node.js (Express server)
- **Backend:**
  - Java, TestNG, RestAssured, Maven, SLF4J, org.json

---

## 5. Test Scenarios

### 5.1 Frontend (Spotify Public Web Player UI)
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Home Page Elements | Navigate to https://open.spotify.com/ | All critical homepage elements (logo, search bar, navigation menu, Create Playlist, Liked Songs, footer) are visible |
| Search Functionality | Enter a search term (e.g., "Linkin Park") in the search bar and submit | Relevant search results (tracks, artists, albums) are displayed |
| Search for Specific Songs | Search for songs like "Zinda", "Give me some Sunshine", etc. | Each song appears in the search results |
| Error Handling | Simulate invalid search or network error | Appropriate error message or empty state is shown |

### 5.2 Backend (API Smoke Tests)
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Search Track | Search for tracks by name/artist/album | Track ID is found for each input |
| Get User ID | Call /me endpoint | User ID is returned |
| Create Playlist | Create playlist with name | Playlist is created, name matches |
| Add Tracks | Add tracks to playlist | Tracks are added successfully |
| Replace Tracks | Replace a track in playlist | Playlist reflects new track list |
| Create Second Playlist | Create another playlist | Playlist is created |
| Add Tracks to Second Playlist | Add tracks | Tracks are added |
| Cleanup | Delete created playlists | Playlists are deleted |

---

## 6. Exhaustive API Test Scenarios

### 6.1 GET /search API

#### 6.1.1 Basic Search Functionality
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Search by Track Name | 1. Send GET request to `/search` with `q=track:Zinda`<br>2. Set `type=track`<br>3. Include valid access token | 1. Status code 200<br>2. Response contains tracks array<br>3. At least one track with name "Zinda" is found<br>4. Track ID is returned |
| Search by Artist | 1. Send GET request to `/search` with `q=artist:Linkin Park`<br>2. Set `type=track`<br>3. Include valid access token | 1. Status code 200<br>2. Response contains tracks array<br>3. Tracks by Linkin Park are returned<br>4. Artist name matches in results |
| Search by Album | 1. Send GET request to `/search` with `q=album:Bhaag Milkha Bhaag`<br>2. Set `type=track`<br>3. Include valid access token | 1. Status code 200<br>2. Response contains tracks array<br>3. Tracks from "Bhaag Milkha Bhaag" album are returned<br>4. Album name matches in results |
| Combined Search | 1. Send GET request to `/search` with `q=track:In The End artist:Linkin Park`<br>2. Set `type=track`<br>3. Include valid access token | 1. Status code 200<br>2. Response contains tracks array<br>3. Specific track "In The End" by Linkin Park is found<br>4. Track details are accurate |

#### 6.1.2 Search Parameters and Pagination
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Limit Results | 1. Send GET request to `/search` with `q=track:test`<br>2. Set `limit=5`<br>3. Include valid access token | 1. Status code 200<br>2. Maximum 5 tracks returned<br>3. `limit` field in response matches 5 |
| Offset Results | 1. Send GET request to `/search` with `q=track:test`<br>2. Set `offset=10`<br>3. Include valid access token | 1. Status code 200<br>2. Results start from 11th item<br>3. `offset` field in response matches 10 |
| Market Filter | 1. Send GET request to `/search` with `q=track:test`<br>2. Set `market=US`<br>3. Include valid access token | 1. Status code 200<br>2. Results are filtered for US market<br>3. Track availability matches US market |

#### 6.1.3 Error Scenarios
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Invalid Access Token | 1. Send GET request to `/search` with `q=track:test`<br>2. Use invalid/expired access token | 1. Status code 401<br>2. Error message indicates authentication failure |
| Missing Query Parameter | 1. Send GET request to `/search` without `q` parameter<br>2. Include valid access token | 1. Status code 400<br>2. Error message indicates missing required parameter |
| Empty Query | 1. Send GET request to `/search` with `q=` (empty)<br>2. Include valid access token | 1. Status code 400<br>2. Error message indicates invalid query |
| Invalid Market Code | 1. Send GET request to `/search` with `q=track:test`<br>2. Set `market=INVALID`<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates invalid market code |

### 6.2 POST /users/{user_id}/playlists API

#### 6.2.1 Basic Playlist Creation
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Create Public Playlist | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include body: `{"name": "Test Playlist", "public": true}`<br>3. Include valid access token | 1. Status code 201<br>2. Playlist is created successfully<br>3. Response contains playlist ID<br>4. Playlist name matches "Test Playlist"<br>5. Public field is true |
| Create Private Playlist | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include body: `{"name": "Private Playlist", "public": false}`<br>3. Include valid access token | 1. Status code 201<br>2. Playlist is created successfully<br>3. Response contains playlist ID<br>4. Playlist name matches "Private Playlist"<br>5. Public field is false |
| Create Playlist with Description | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include body: `{"name": "Test Playlist", "description": "Test description", "public": true}`<br>3. Include valid access token | 1. Status code 201<br>2. Playlist is created successfully<br>3. Description field matches "Test description" |

#### 6.2.2 Playlist Creation with Different Names
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Create Playlist with Special Characters | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include body: `{"name": "Test Playlist!@#$%", "public": true}`<br>3. Include valid access token | 1. Status code 201<br>2. Playlist is created with special characters in name |
| Create Playlist with Long Name | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include body: `{"name": "Very Long Playlist Name That Exceeds Normal Length", "public": true}`<br>3. Include valid access token | 1. Status code 201<br>2. Playlist is created with long name |
| Create Multiple Playlists | 1. Create first playlist with name "Playlist 1"<br>2. Create second playlist with name "Playlist 2"<br>3. Include valid access token | 1. Both playlists created successfully<br>2. Different playlist IDs returned<br>3. Names match respectively |

#### 6.2.3 Error Scenarios
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Invalid User ID | 1. Send POST request to `/users/invalid_user_id/playlists`<br>2. Include valid playlist body<br>3. Include valid access token | 1. Status code 404<br>2. Error message indicates user not found |
| Missing Playlist Name | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include body: `{"public": true}` (no name)<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates missing required field |
| Empty Playlist Name | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include body: `{"name": "", "public": true}`<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates invalid playlist name |
| Invalid Access Token | 1. Send POST request to `/users/{user_id}/playlists`<br>2. Include valid playlist body<br>3. Use invalid/expired access token | 1. Status code 401<br>2. Error message indicates authentication failure |
| Insufficient Permissions | 1. Send POST request to `/users/{other_user_id}/playlists`<br>2. Include valid playlist body<br>3. Include valid access token for different user | 1. Status code 403<br>2. Error message indicates insufficient permissions |

### 6.3 POST /playlists/{playlist_id}/tracks API

#### 6.3.1 Basic Track Addition
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Add Single Track | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1"]}`<br>3. Include valid access token | 1. Status code 201<br>2. Track is added to playlist<br>3. Playlist snapshot ID is returned |
| Add Multiple Tracks | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1", "spotify:track:track_id_2"]}`<br>3. Include valid access token | 1. Status code 201<br>2. All tracks are added to playlist<br>3. Playlist snapshot ID is returned |
| Add Tracks with Position | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1"], "position": 0}`<br>3. Include valid access token | 1. Status code 201<br>2. Track is added at specified position<br>3. Track appears first in playlist |

#### 6.3.2 Track Addition with Different URIs
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Add Tracks by URI | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:4iV5W9uYEdYUVa79Axb7Rh"]}`<br>3. Include valid access token | 1. Status code 201<br>2. Track is added successfully<br>3. Track URI matches in playlist |
| Add Tracks by ID | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:4iV5W9uYEdYUVa79Axb7Rh"]}`<br>3. Include valid access token | 1. Status code 201<br>2. Track is added successfully<br>3. Track ID is correctly mapped |

#### 6.3.3 Error Scenarios
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Invalid Playlist ID | 1. Send POST request to `/playlists/invalid_playlist_id/tracks`<br>2. Include valid track body<br>3. Include valid access token | 1. Status code 404<br>2. Error message indicates playlist not found |
| Invalid Track URI | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:invalid_track_id"]}`<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates invalid track URI |
| Empty URIs Array | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": []}`<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates empty URIs array |
| Duplicate Track URIs | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1", "spotify:track:track_id_1"]}`<br>3. Include valid access token | 1. Status code 201<br>2. Duplicate tracks are handled gracefully |
| Invalid Access Token | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include valid track body<br>3. Use invalid/expired access token | 1. Status code 401<br>2. Error message indicates authentication failure |
| Insufficient Permissions | 1. Send POST request to `/playlists/{playlist_id}/tracks`<br>2. Include valid track body<br>3. Use access token without playlist-modify-public/private scope | 1. Status code 403<br>2. Error message indicates insufficient permissions |

### 6.4 PUT /playlists/{playlist_id}/tracks API

#### 6.4.1 Basic Track Replacement
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Replace All Tracks | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1", "spotify:track:track_id_2"]}`<br>3. Include valid access token | 1. Status code 200<br>2. All existing tracks are replaced<br>3. Only new tracks remain in playlist<br>4. Playlist snapshot ID is returned |
| Replace with Single Track | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1"]}`<br>3. Include valid access token | 1. Status code 200<br>2. Playlist contains only the specified track<br>3. All previous tracks are removed |
| Replace with Empty Playlist | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": []}`<br>3. Include valid access token | 1. Status code 200<br>2. All tracks are removed from playlist<br>3. Playlist becomes empty |

#### 6.4.2 Track Replacement with Position
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Replace Tracks at Specific Position | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1"], "range_start": 0, "insert_before": 1}`<br>3. Include valid access token | 1. Status code 200<br>2. Tracks are replaced at specified position<br>3. New tracks are inserted before specified index |
| Replace Tracks in Range | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1"], "range_start": 0, "range_length": 2}`<br>3. Include valid access token | 1. Status code 200<br>2. Tracks in specified range are replaced<br>3. Range length determines how many tracks to replace |

#### 6.4.3 Error Scenarios
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Invalid Playlist ID | 1. Send PUT request to `/playlists/invalid_playlist_id/tracks`<br>2. Include valid track body<br>3. Include valid access token | 1. Status code 404<br>2. Error message indicates playlist not found |
| Invalid Track URI | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:invalid_track_id"]}`<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates invalid track URI |
| Invalid Range Parameters | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1"], "range_start": -1}`<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates invalid range parameters |
| Range Exceeds Playlist Length | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include body: `{"uris": ["spotify:track:track_id_1"], "range_start": 0, "range_length": 100}`<br>3. Include valid access token | 1. Status code 400<br>2. Error message indicates range exceeds playlist length |
| Invalid Access Token | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include valid track body<br>3. Use invalid/expired access token | 1. Status code 401<br>2. Error message indicates authentication failure |
| Insufficient Permissions | 1. Send PUT request to `/playlists/{playlist_id}/tracks`<br>2. Include valid track body<br>3. Use access token without playlist-modify-public/private scope | 1. Status code 403<br>2. Error message indicates insufficient permissions |

### 6.5 DELETE /playlists/{playlist_id}/followers API

#### 6.5.1 Basic Playlist Deletion
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Delete Own Playlist | 1. Send DELETE request to `/playlists/{playlist_id}/followers`<br>2. Include valid access token for playlist owner | 1. Status code 200<br>2. Playlist is successfully deleted<br>3. Playlist is no longer accessible |
| Delete Public Playlist | 1. Send DELETE request to `/playlists/{playlist_id}/followers`<br>2. Include valid access token for playlist owner | 1. Status code 200<br>2. Public playlist is deleted<br>3. Playlist disappears from public listings |
| Delete Private Playlist | 1. Send DELETE request to `/playlists/{playlist_id}/followers`<br>2. Include valid access token for playlist owner | 1. Status code 200<br>2. Private playlist is deleted<br>3. Playlist is completely removed |

#### 6.5.2 Playlist Deletion Verification
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Verify Playlist Deletion | 1. Delete playlist using DELETE request<br>2. Attempt to access playlist with GET request<br>3. Include valid access token | 1. DELETE returns status 200<br>2. GET request returns 404<br>3. Playlist is confirmed deleted |
| Delete Non-Existent Playlist | 1. Send DELETE request to `/playlists/non_existent_id/followers`<br>2. Include valid access token | 1. Status code 404<br>2. Error message indicates playlist not found |

#### 6.5.3 Error Scenarios
| Scenario | Steps | Expected Result |
|----------|-------|----------------|
| Invalid Playlist ID | 1. Send DELETE request to `/playlists/invalid_playlist_id/followers`<br>2. Include valid access token | 1. Status code 404<br>2. Error message indicates playlist not found |
| Delete Other User's Playlist | 1. Send DELETE request to `/playlists/{other_user_playlist_id}/followers`<br>2. Include valid access token for different user | 1. Status code 403<br>2. Error message indicates insufficient permissions |
| Invalid Access Token | 1. Send DELETE request to `/playlists/{playlist_id}/followers`<br>2. Use invalid/expired access token | 1. Status code 401<br>2. Error message indicates authentication failure |
| Delete Already Deleted Playlist | 1. Delete playlist successfully<br>2. Attempt to delete same playlist again<br>3. Include valid access token | 1. First DELETE returns 200<br>2. Second DELETE returns 404<br>3. Error message indicates playlist not found |

---

## 7. References
- **Frontend Tests:**
  - `Spotify/src/test/java/tests/SpotifyUiTest.java`
- **Backend Tests:**
  - `Spotify/src/test/java/tests/SpotifySmokeTest.java`
- **Readme & Setup:**
  - `Spotify/Readme.md`
  - `Spotify/SPOTIFY_AUTHORIZATION_SETUP.md`

---

## 8. How to Extend
- **Backend:**
  - Add new test methods for additional API flows as needed.
- **Frontend:**
  - Add new test data to DataProviders in the test classes.

---

## 9. Notes
- All API calls require valid OAuth tokens with appropriate scopes.
- Clean up test data (playlists) after test execution to avoid polluting user accounts.
- For UI, manual or automated (Selenium) tests can be added for more coverage. 