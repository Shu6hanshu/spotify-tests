package util;

public enum SpotifyApiEndpoint {
    SEARCH_TRACK("/search", "GET", "SearchTrackRequest", "SearchTrackResponse"),
    CREATE_PLAYLIST("/users/{user_id}/playlists", "POST", "CreatePlaylistRequest", "CreatePlaylistResponse"),
    ADD_TRACKS_TO_PLAYLIST("/playlists/{playlist_id}/tracks", "POST", "AddTracksRequest", "AddTracksResponse"),
    REPLACE_TRACKS_IN_PLAYLIST("/playlists/{playlist_id}/tracks", "PUT", "ReplaceTracksRequest", "ReplaceTracksResponse"),
    DELETE_PLAYLIST("/playlists/{playlist_id}/followers", "DELETE", null, "DeletePlaylistResponse");

    private final String route;
    private final String method;
    private final String requestMethod;
    private final String responseMethod;

    SpotifyApiEndpoint(String route, String method, String requestMethod, String responseMethod) {
        this.route = route;
        this.method = method;
        this.requestMethod = requestMethod;
        this.responseMethod = responseMethod;
    }

    public String getRoute() {
        return route;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getResponseMethod() {
        return responseMethod;
    }
} 