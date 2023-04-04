package Kopeav.MusicApp.Controllers;

import Kopeav.MusicApp.DTO.SongDTO;
import Kopeav.MusicApp.Models.Song;
import Kopeav.MusicApp.Services.SongService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
@AllArgsConstructor
@CrossOrigin
public class SongController {

    private SongService songService;

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSong(@PathVariable int id) {
        Optional<Song> songOptional = songService.findSongById(id);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();

            String songData =  Base64.getEncoder().encodeToString(song.getSong());
            String iconData = Base64.getEncoder().encodeToString(song.getIcon());

            SongDTO requestSong = SongDTO.builder()
                    .id(song.getId())
                    .name(song.getName())
                    .author(song.getAuthor())
                    .songData(songData)
                    .iconData(iconData).build();

            return ResponseEntity.ok()
                    .body(requestSong);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/{id}/song")
//    public ResponseEntity<byte[]> getSong(@PathVariable int id) {
//        Optional<Song> optionalSong = songService.findSongById(id);
//
//        if (optionalSong.isPresent()) {
//            Song song = optionalSong.get();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentLength(song.getSong().length);
//            return new ResponseEntity<>(song.getSong(), headers, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @PostMapping("/upload")
    public ResponseEntity<String> saveNewSong(@RequestParam("song") MultipartFile song,
                                              @RequestParam("icon") MultipartFile icon,
                                              @RequestParam("author") String author,
                                              @RequestParam("name") String name) {
        try {
            Song newSong = Song.builder()
                    .song(song.getBytes())
                    .icon(icon.getBytes())
                    .name(name)
                    .author(author)
                    .build();

            songService.save(newSong);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add new song");
        }
    }

}
