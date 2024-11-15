package telran.album.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.album.dao.Album;
import telran.album.dao.AlbumImpl;
import telran.album.model.Photo;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {
    private Photo[] photos;
    private final int capacity = 6;
    private Album album;
    private final LocalDateTime now = LocalDateTime.now();
    private final Comparator<Photo> comparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAlbumId(), p2.getAlbumId());
        return res != 0 ? res : Integer.compare(p1.getPhotoId(), p2.getPhotoId());
    };

    @BeforeEach
    void setUp() {
        album = new AlbumImpl(capacity);
        photos = new Photo[capacity];
        photos[0] = new Photo(1, 1, "Photo1", "url1", now.minusDays(7));
        photos[1] = new Photo(1, 2, "Photo2", "url2", now.minusDays(6));
        photos[2] = new Photo(1, 3, "Photo3", "url3", now.minusDays(5));
        photos[3] = new Photo(2, 1, "Photo4", "url4", now.minusDays(4));
        photos[4] = new Photo(2, 4, "Photo5", "url5", now.minusDays(3));
        for (int i = 0; i < photos.length; i++) {
            album.addPhoto(photos[i]);
        }
    }

    @Test
    void testAddPhoto() {
        assertFalse(album.addPhoto(null));
        assertFalse(album.addPhoto(photos[4]));
        Photo photo = new Photo(2, 2, "Photo6", "url6", now.minusDays(1));
        assertTrue(album.addPhoto(photo));
        assertEquals(capacity, album.size());
        assertFalse(album.addPhoto(new Photo(3, 1, "Photo7", "url7", now.minusDays(1))));
    }

    @Test
    void testRemovePhoto() {
        assertTrue(album.removePhoto(3, 1));
        assertNull(album.getPhotoFromAlbum(3,1));
        assertEquals(4, album.size());
        assertFalse(album.removePhoto(1, 3));
    }

    @Test
    void testUpdatePhoto() {
        assertFalse(album.updatePhoto(1, 5, "newPhoto"));
        assertFalse(album.updatePhoto(5, 1, "newPhoto"));
        assertTrue(album.updatePhoto(1, 1, "newUrl"));
        assertEquals("newUrl", album.getPhotoFromAlbum(1, 1).getUrl());
    }

    @Test
    void testGetPhotoFromAlbum() {
        assertEquals(photos[3], album.getPhotoFromAlbum(1, 2));
        assertNull(album.getPhotoFromAlbum(4, 3));
    }

    @Test
    void testGetAllPhotoFromAlbum() {
        Photo[] actual = album.getAllPhotoFromAlbum(2);
        Arrays.sort(actual, comparator);
        Photo[] expected = {photos[3], photos[4]};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testGetPhotoBetweenDate() {
        Photo[] actual = album.getPhotoBetweenDate(now.minusDays(7).toLocalDate(), now.minusDays(4).toLocalDate());
        Arrays.sort(actual, comparator);
        Photo[] expected = {photos[0], photos[1], photos[2]};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSize() {
        assertEquals(capacity - 1, album.size());
    }
}