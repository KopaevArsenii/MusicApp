package Kopeav.MusicApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongDTO {
    private int id;
    private String name;
    private String author;
    private String songData;
    private String iconData;

}
