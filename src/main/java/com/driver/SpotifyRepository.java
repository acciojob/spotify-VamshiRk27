package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user=new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist=new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist=null;
        for(Artist profile:artists){
            if(profile.getName().equals(artistName)){
                artist=profile;
            }
        }
        if(artist==null){
            artist=createArtist(artistName);
        }
        List<Album> list=artistAlbumMap.get(artist);
        if(list==null){
            list=new ArrayList<>();
        }
        //For artistAlbumMap Db
        if(!artistAlbumMap.containsKey(artist)){
            artistAlbumMap.put(artist,list);
        }
        //New Album
        Album album=new Album(title);
        //Adding into list of all Albums
        albums.add(album);
        //Adding into Album's list of artist(HashMap)
        list.add(album);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song=new Song(title,length);
        songs.add(song);
        Album album=null;
        for(Album profile:albums){
            if(profile.getTitle().equals(albumName)){
                album=profile;
                break;
            }
        }
        if(album==null){
            throw new Exception("Album does not exist");
        }
        //For list of Songs in th album
        List<Song> list=albumSongMap.get(album);
        if(list==null){
            list=new ArrayList<>();
        }
        //Adding into albumSongMap Db
        if(!albumSongMap.containsKey(album)){
            albumSongMap.put(album,list);
        }
        list.add(song);
        songLikeMap.put(song,new ArrayList<>());
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist profile:playlists){
            if(profile.getTitle().equals(title)){
                return profile;
            }
        }
        Playlist playlist=new Playlist(title);
        User user=null;
        for(User profile:users){
            if(profile.getMobile().equals(mobile)){
                user=profile;
                break;
            }
        }
        if(user==null){
            throw new Exception("User does not exist");
        }
        playlists.add(playlist);
        List<Song> list=playlistSongMap.get(playlist);
        if(list==null){
            list=new ArrayList<>();
            playlistSongMap.put(playlist,list);
        }
        for(Song song:songs){
            if(song.getLength()==length){
                list.add(song);
            }
        }
        creatorPlaylistMap.put(user,playlist);

        List<Playlist> playlistList=userPlaylistMap.get(user);
        if(playlistList==null){
            playlistList=new ArrayList<>();
            userPlaylistMap.put(user,playlistList);
        }
        playlistList.add(playlist);

        List<User> usersList=playlistListenerMap.get(playlist);
        if(usersList==null){
            usersList=new ArrayList<>();
            playlistListenerMap.put(playlist,usersList);
        }
        usersList.add(user);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist profile:playlists){
            if(profile.getTitle().equals(title)){
                return profile;
            }
        }
        Playlist playlist=new Playlist(title);
        User user=null;
        for(User profile:users){
            if(profile.getMobile().equals(mobile)){
                user=profile;
                break;
            }
        }
        if(user==null){
            throw new Exception("User does not exist");
        }
        playlists.add(playlist);
        List<Song> list=new ArrayList<>();
        for(String songTitle:songTitles){
            for(Song song:songs){
                if(song.getTitle().equals(songTitle)){
                    list.add(song);
                }
            }
        }
        creatorPlaylistMap.put(user,playlist);

        List<Playlist> playlistList=userPlaylistMap.get(user);
        if(playlistList==null){
            playlistList=new ArrayList<>();
            userPlaylistMap.put(user,playlistList);
        }
        playlistList.add(playlist);

        playlistSongMap.put(playlist,list);
        List<User> usersList=playlistListenerMap.get(playlist);
        if(usersList==null){
            usersList=new ArrayList<>();
            playlistListenerMap.put(playlist,usersList);
        }
        usersList.add(user);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist playlist=null;
        User user=null;
        for(Playlist profile:playlists){
            if(profile.getTitle().equals(playlistTitle)){
                playlist=profile;
                break;
            }
        }
        for(User profile:users){
            if(profile.getMobile().equals(mobile)){
                user=profile;
                break;
            }
        }
        if(user==null){
            throw new Exception("User does not exist");
        }
        if(playlist==null){
            throw new Exception("Playlist does not exist");
        }
        //The user is a creator of the playlist and then in turn will be the listener of the playlist
        if((creatorPlaylistMap.containsKey(user) && creatorPlaylistMap.get(user).equals(playlist))){
            return playlist;
        }
        //If user isn't the creator but is a listener of the playlist
        List<User> userList=playlistListenerMap.get(playlist);
        for(User profile:userList){
            if(profile==user){
                return playlist;
            }
        }
        //if user isn't a creator and listener of the playlist we update the listener
        userList.add(user);
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user=null;
        Song song=null;
        for(User profile:users){
            if(profile.getMobile().equals(mobile)){
                user=profile;
                break;
            }
        }
        for(Song profile:songs){
            if(profile.getTitle().equals(songTitle)){
                song=profile;
                break;
            }
        }
        if(user==null){
            throw new Exception("User does not exist");
        }
        if(song==null){
            throw new Exception("Song does not exist");
        }
        List<User> userLikeList=songLikeMap.get(song);
        // If user already liked the song and exists in the list
        for(User profile: userLikeList){
            if(profile==user){
                return song;
            }
        }
        userLikeList.add(user);
        Album album=null;
        for(Album albumProfile:albumSongMap.keySet()){
            List<Song> list=albumSongMap.get(albumProfile);
            for(Song profile:list){
                if(profile == song) {
                    album = albumProfile;
                    break;
                }
            }
        }
        Artist artist=null;
        for(Artist artistProfile:artistAlbumMap.keySet()){
            List<Album> list=artistAlbumMap.get(artistProfile);
            for(Album profile:list){
                if(profile==album){
                    artist=artistProfile;
                    break;
                }
            }
        }
        song.setLikes(song.getLikes()+1);
        if(artist!=null){
            artist.setLikes(artist.getLikes()+1);
            return song;
        }
        return null;
    }

    public String mostPopularArtist() {
        int maxLikes=0;
        Artist artist=null;
        for(Artist profile:artists){
            if(profile.getLikes()>maxLikes){
                maxLikes=profile.getLikes();
                artist=profile;
            }
        }
        if(artist!=null){
            return artist.getName();
        }
        return null;
    }

    public String mostPopularSong() {
        Song song=null;
        int maxLikes=0;
        for (Song profile:songLikeMap.keySet()){
            int count=songLikeMap.get(profile).size();
            if(count>maxLikes){
                maxLikes=count;
                song=profile;
            }
        }
        if(song!=null){
            return song.getTitle();
        }
        return null;
    }

    // Additional
    // Extra
    // Functions

    public HashMap<Artist, List<Album>> getArtistAlbumMap() {
        return artistAlbumMap;
    }

    public HashMap<Album, List<Song>> getAlbumSongMap() {
        return albumSongMap;
    }

    public HashMap<Playlist, List<Song>> getPlaylistSongMap() {
        return playlistSongMap;
    }

    public HashMap<Playlist, List<User>> getPlaylistListenerMap() {
        return playlistListenerMap;
    }

    public HashMap<User, Playlist> getCreatorPlaylistMap() {
        return creatorPlaylistMap;
    }

    public HashMap<User, List<Playlist>> getUserPlaylistMap() {
        return userPlaylistMap;
    }

    public HashMap<Song, List<User>> getSongLikeMap() {
        return songLikeMap;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Artist> getArtists() {
        return artists;
    }
}
