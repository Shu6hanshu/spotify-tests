package pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pojo.entity.Track;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchTrackResponse {
    private String href;
    private Integer limit;
    private String next;
    private Integer offset;
    private String previous;
    private Integer total;
    private java.util.List<Track> items;
}
