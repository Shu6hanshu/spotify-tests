package pojo.entity;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    private ExternalUrls externalUrls;
    private Followers followers;
    private List<String> genres;
    private String href;
    private String id;
    private List<ImageObject> images;
    private String name;
    private Integer popularity;
    private String type;
    private String uri;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExternalUrls {
        private String spotify;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Followers {
        private String href; // Nullable, always null
        private Integer total;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageObject {
        private String url;
        private Integer height;
        private Integer width;
    }
} 