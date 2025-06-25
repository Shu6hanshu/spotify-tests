package util;

import org.testng.Assert;
import pojo.Album;
import pojo.Artist;
import pojo.Track;

public class SpotifyAssertions {
    public static void assertTrack(Track track, String expectedName) {
        Assert.assertNotNull(track, "Track should not be null");
        Assert.assertEquals(track.getName(), expectedName, "Track name mismatch");
    }

    public static void assertArtist(Artist artist, String expectedName) {
        Assert.assertNotNull(artist, "Artist should not be null");
        Assert.assertEquals(artist.getName(), expectedName, "Artist name mismatch");
    }

    public static void assertAlbum(Album album, String expectedName) {
        Assert.assertNotNull(album, "Album should not be null");
        Assert.assertEquals(album.getName(), expectedName, "Album name mismatch");
    }

    public static void assertStatusCode(int actual, int expected) {
        Assert.assertEquals(actual, expected, "Status code mismatch");
    }
} 