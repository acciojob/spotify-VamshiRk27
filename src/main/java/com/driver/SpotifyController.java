package com.driver;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spotify")
public class SpotifyController {

    //Autowired will not work in this case, no need to change this and add auto-wire
    SpotifyService spotifyService = new SpotifyService();

    @PostMapping("/add-user")
    public String createUser(@RequestParam(name = "name") String name,@RequestParam("mobile") String mobile){
        //create the user with given name and number
        User user=spotifyService.createUser(name,mobile);
        if(user==null){
            return null;
        }
        return "Success";
    }

    @PostMapping("/add-artist")
    public String createArtist(@RequestParam(name = "name") String name){
        //create the artist with given name
        Artist artist=spotifyService.createArtist(name);
        if(artist==null){
            return null;
        }
        return "Success";
    }

    @PostMapping("/add-album")
    public String createAlbum(@RequestParam(name = "title") String title,@RequestParam("artistName") String artistName){
        //If the artist does not exist, first create an artist with given name
        //Create an album with given title and artist
        Album album=spotifyService.createAlbum(title, artistName);
        if(album==null){
            return null;
        }
        return "Success";
    }

    @PostMapping("/add-song")
    public String createSong(@RequestParam("title") String title,@RequestParam("albumName") String albumName,@RequestParam("length") int length) throws Exception{
        //If the album does not exist in database, throw "Album does not exist" exception
        //Create and add the song to respective album
        Song song=spotifyService.createSong(title,albumName,length);
        if(song==null){
            return null;
        }
        return "Success";
    }

    @PostMapping("/add-playlist-on-length")
    public String createPlaylistOnLength(@RequestParam("mobile") String mobile,@RequestParam("title") String title,@RequestParam("length") int length) throws Exception{
        //Create a playlist with given title and add all songs having the given length in the database to that playlist
        //The creator of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        Playlist playlist=spotifyService.createPlaylistOnLength(mobile,title,length);
        return "Success";
    }

    @PostMapping("/add-playlist-on-name")
    public String createPlaylistOnName(@RequestParam("mobile")String mobile,@RequestParam("title") String title,@RequestParam("songTitles") List<String> songTitles) throws Exception{
        //Create a playlist with given title and add all songs having the given titles in the database to that playlist
        //The creator of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        Playlist playlist=spotifyService.createPlaylistOnName(mobile,title,songTitles);
        return "Success";
    }

    @PutMapping("/find-playlist")
    public String findPlaylist(@RequestParam("mobile") String mobile,@RequestParam("playlistTitle") String playlistTitle) throws Exception{
        //Find the playlist with given title and add user as listener of that playlist and update user accordingly
        //If the user is creator or already a listener, do nothing
        //If the user does not exist, throw "User does not exist" exception
        //If the playlist does not exist, throw "Playlist does not exist" exception
        // Return the playlist after updating
        Playlist playlist=spotifyService.findPlaylist(mobile,playlistTitle);
        return "Success";
    }

    @PutMapping("/like-song")
    public String likeSong(@RequestParam("mobile") String mobile,@RequestParam("songTitle") String songTitle) throws Exception{
        //The user likes the given song. The corresponding artist of the song gets auto-liked
        //A song can be liked by a user only once. If a user tried to like a song multiple times, do nothing
        //However, an artist can indirectly have multiple likes from a user, if the user has liked multiple songs of that artist.
        //If the user does not exist, throw "User does not exist" exception
        //If the song does not exist, throw "Song does not exist" exception
        //Return the song after updating
        Song song=spotifyService.likeSong(mobile,songTitle);
        return "Success";
    }

    @GetMapping("/popular-artist")
    public String mostPopularArtist(){
        //Return the artist name with maximum likes
        return spotifyService.mostPopularArtist();
    }

    @GetMapping("/popular-song")
    public String mostPopularSong(){
        //return the song title with maximum likes
        return spotifyService.mostPopularSong();
    }


    // Additional
    // Extra
    // Functions
    @GetMapping("/get-artistAlbum")
    public HashMap<Artist, List<Album>> getArtistAlbumMap() {
        return spotifyService.getArtistAlbumMap();
    }
    @GetMapping("/get-albumSong")
    public HashMap<Album, List<Song>> getAlbumSongMap() {
        return spotifyService.getAlbumSongMap();
    }
    @GetMapping("/get-playlistSong")
    public HashMap<Playlist, List<Song>> getPlaylistSongMap() {
        return spotifyService.getPlaylistSongMap();
    }
    @GetMapping("/get-playlistListener")
    public HashMap<Playlist, List<User>> getPlaylistListenerMap() {
        return spotifyService.getPlaylistListenerMap();
    }
    @GetMapping("/get-creatorPlaylist")
    public HashMap<User, Playlist> getCreatorPlaylistMap() {
        return spotifyService.getCreatorPlaylistMap();
    }
    @GetMapping("/get-userPlaylist")
    public HashMap<User, List<Playlist>> getUserPlaylistMap() {
        return spotifyService.getUserPlaylistMap();
    }
    @GetMapping("/get-songLike")
    public HashMap<Song, List<User>> getSongLikeMap() {
        return spotifyService.getSongLikeMap();
    }
    @GetMapping("/get-users")
    public List<User> getUsers() {
        return spotifyService.getUsers();
    }
    @GetMapping("/get-songs")
    public List<Song> getSongs() {
        return spotifyService.getSongs();
    }
    @GetMapping("/get-playlists")
    public List<Playlist> getPlaylists() {
        return spotifyService.getPlaylists();
    }
    @GetMapping("/get-albums")
    public List<Album> getAlbums() {
        return spotifyService.getAlbums();
    }
    @GetMapping("/get-artists")
    public List<Artist> getArtists() {
        return spotifyService.getArtists();
    }
}
