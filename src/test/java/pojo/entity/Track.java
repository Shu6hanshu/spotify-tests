package pojo.entity;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import pojo.entity.Album;
import pojo.entity.Artist;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private Album album;
    private List<Album.SimplifiedArtist> artists;
    private List<String> availableMarkets;
    private Integer discNumber;
    private Integer durationMs;
    private Boolean explicit;
    private Artist.ExternalUrls externalUrls;
    private ExternalIds externalIds;
    private String href;
    private String id;
    private Boolean isPlayable;
    private LinkedFrom linkedFrom;
    private Album.Restrictions restrictions;
    private String name;
    private Integer popularity;
    private String previewUrl;
    private Integer trackNumber;
    private String type;
    private String uri;
    private Boolean isLocal;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExternalIds {
        private String isrc;
        private String ean;
        private String upc;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LinkedFrom {
        // This can be another Track object or a subset; for now, just use id and href
        private String id;
        private String href;
    }
} 