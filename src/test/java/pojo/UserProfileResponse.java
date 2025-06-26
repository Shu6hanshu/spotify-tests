package pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String country;
    private String display_name;
    private String email;
    private ExplicitContent explicit_content;
    private ExternalUrls external_urls;
    private Followers followers;
    private String href;
    private String id;
    private List<ImageObject> images;
    private String product;
    private String type;
    private String uri;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExplicitContent {
        private boolean filter_enabled;
        private boolean filter_locked;
    }

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
        private String href;
        private int total;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageObject {
        private String url;
        private int height;
        private int width;
    }
} 