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

## 6. References
- **Frontend Tests:**
  - `Spotify/src/test/java/tests/SpotifyUiTest.java`
- **Backend Tests:**
  - `Spotify/src/test/java/tests/SpotifySmokeTest.java`
- **Readme & Setup:**
  - `Spotify/Readme.md`
  - `Spotify/SPOTIFY_AUTHORIZATION_SETUP.md`

---

## 7. How to Extend
- **Backend:**
  - Add new test methods for additional API flows as needed.
- **Frontend:**
  - Add new test data to DataProviders in the test classes.

---

## 8. Notes
- All API calls require valid OAuth tokens with appropriate scopes.
- Clean up test data (playlists) after test execution to avoid polluting user accounts.
- For UI, manual or automated (Selenium) tests can be added for more coverage. 