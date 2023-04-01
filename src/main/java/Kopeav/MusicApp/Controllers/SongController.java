package Kopeav.MusicApp.Controllers;

import Kopeav.MusicApp.Models.Song;
import Kopeav.MusicApp.Repositories.SongRepository;
import Kopeav.MusicApp.Services.SongService;
import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
@AllArgsConstructor
@CrossOrigin
public class SongController {

    private ServletContext servletContext;
    private SongService songService;
    private ResourceLoader resourceLoader;

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable int id) {
        Optional<Song> songOptional = songService.findSongById(id);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();

            return ResponseEntity.ok()
                    .body(song);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listen/{fileName}")
    public ResponseEntity<?> getMusic(@PathVariable String fileName) {
        Resource resource = resourceLoader.getResource("classpath:/static/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/");
            File folder = resource.getFile();
            File newFile = new File(folder.getAbsolutePath() + "/" + file.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(newFile)) {
                os.write(file.getBytes());
            }
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

}
