package util;

import org.testng.Assert;
import pojo.entity.Album;
import pojo.entity.Artist;
import pojo.entity.Track;

public class SpotifyAssertions {
    public enum AssertionType {
        NOT_NULL, EQUALS, NULL, CONTAINS_IGNORE_CASE
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
            case CONTAINS_IGNORE_CASE:
                if (actual == null || expected == null) {
                    Assert.fail(message + " (One of the values is null)");
                }
                String actualStr = actual.toString().toLowerCase();
                String expectedStr = expected.toString().toLowerCase();
                Assert.assertTrue(actualStr.contains(expectedStr), message + " (Expected to find '" + expected + "' in '" + actual + "' ignoring case)");
                break;
            default:
                throw new IllegalArgumentException("Unsupported assertion type: " + type);
        }
    }

    public static void assertTrack(Track track, String expectedName) {
        assertValue(track, null, "Track should not be null", AssertionType.NOT_NULL);
        assertValue(track.getName(), expectedName, "Track name mismatch", AssertionType.CONTAINS_IGNORE_CASE);
    }

    public static void assertArtist(Album.SimplifiedArtist artist, String expectedName) {
        assertValue(artist, null, "Artist should not be null", AssertionType.NOT_NULL);
        assertValue(artist.getName(), expectedName, "Artist name mismatch", AssertionType.CONTAINS_IGNORE_CASE);
    }

    public static void assertAlbum(Album album, String expectedName) {
        assertValue(album, null, "Album should not be null", AssertionType.NOT_NULL);
        assertValue(album.getName(), expectedName, "Album name mismatch", AssertionType.CONTAINS_IGNORE_CASE);
    }

    public static void assertStatusCode(int actual, int expected) {
        assertValue(actual, expected, "Status code mismatch", AssertionType.EQUALS);
    }
} 