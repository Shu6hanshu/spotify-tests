package pojo.entity;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import pojo.entity.Artist;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private String albumType;
    private Integer totalTracks;
    private List<String> availableMarkets;
    private Artist.ExternalUrls externalUrls;
    private String href;
    private String id;
    private List<Artist.ImageObject> images;
    private String name;
    private String releaseDate;
    private String releaseDatePrecision;
    private Restrictions restrictions;
    private String type;
    private String uri;
    private List<SimplifiedArtist> artists;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Restrictions {
        private String reason;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimplifiedArtist {
        private Artist.ExternalUrls externalUrls;
        private String href;
        private String id;
        private String name;
        private String type;
        private String uri;
    }
} 