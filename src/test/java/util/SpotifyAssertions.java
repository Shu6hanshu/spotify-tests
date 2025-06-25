package util;

import org.testng.Assert;
import pojo.entity.Album;
import pojo.entity.Artist;
import pojo.entity.Track;

public class SpotifyAssertions {
    public enum AssertionType {
        NOT_NULL, EQUALS, NULL
    }

    public static void assertValue(Object actual, Object expected, String message, AssertionType type) {
        switch (type) {
            case NOT_NULL:
                Assert.assertNotNull(actual, message);
                break;
            case NULL:
                Assert.assertNull(actual, message);
                break;
            case EQUALS:
                Assert.assertEquals(actual, expected, message);
                break;
            default:
                throw new IllegalArgumentException("Unsupported assertion type: " + type);
        }
    }

    public static void assertTrack(Track track, String expectedName) {
        assertValue(track, null, "Track should not be null", AssertionType.NOT_NULL);
        assertValue(track.getName(), expectedName, "Track name mismatch", AssertionType.EQUALS);
    }

    public static void assertArtist(Artist artist, String expectedName) {
        assertValue(artist, null, "Artist should not be null", AssertionType.NOT_NULL);
        assertValue(artist.getName(), expectedName, "Artist name mismatch", AssertionType.EQUALS);
    }

    public static void assertAlbum(Album album, String expectedName) {
        assertValue(album, null, "Album should not be null", AssertionType.NOT_NULL);
        assertValue(album.getName(), expectedName, "Album name mismatch", AssertionType.EQUALS);
    }

    public static void assertStatusCode(int actual, int expected) {
        assertValue(actual, expected, "Status code mismatch", AssertionType.EQUALS);
    }
} 