package util;

public enum SpotifyApiEndpoint {
    SEARCH_TRACK("/search", "GET", "SearchTrackRequest", "SearchTrackResponse"),
    CREATE_PLAYLIST("/users/{user_id}/playlists", "POST", "CreatePlaylistRequest", "CreatePlaylistResponse"),
    ADD_TRACKS_TO_PLAYLIST("/playlists/{playlist_id}/tracks", "POST", "AddTracksRequest", "AddTracksResponse"),
    REPLACE_TRACKS_IN_PLAYLIST("/playlists/{playlist_id}/tracks", "PUT", "ReplaceTracksRequest", "ReplaceTracksResponse"),
    DELETE_PLAYLIST("/playlists/{playlist_id}/followers", "DELETE", null, "DeletePlaylistResponse");

    private final String route;
    private final String method;
    private final String requestClass;
    private final String responseClass;

    SpotifyApiEndpoint(String route, String method, String requestClass, String responseClass) {
        this.route = route;
        this.method = method;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    public String getRoute() {
        return route;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestClass() {
        return requestClass;
    }

    public String getResponseClass() {
        return responseClass;
    }
} 