package telran.album.dao;

import telran.album.model.Photo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.Predicate;

public class AlbumImpl implements Album {
    private Photo[] photos;
    private int size;

    public AlbumImpl(int capacity) {
        photos = new Photo[capacity];
    }

    @Override
    public boolean addPhoto(Photo photo) {
        if (photo == null || photos.length == size || getPhotoFromAlbum(photo.getPhotoId(), photo.getAlbumId()) != null) {
            return false;
        }
        photos[size++] = photo;
        return true;
    }

    @Override
    public boolean removePhoto(int photoId, int albumId) {
        if (getPhotoFromAlbum(photoId, albumId) != null) {
            for (int i = 0; i < size; i++) {
                if (photos[i].getPhotoId() == photoId && photos[i].getAlbumId() == albumId) {
                    //photos[i] = photos[--size];
                    System.arraycopy(photos, i + 1, photos, i, size - i - 1);
                    photos[--size] = null;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updatePhoto(int photoId, int albumId, String url) {
        if (getPhotoFromAlbum(photoId, albumId) != null) {
            for (int i = 0; i < size; i++) {
                if (photos[i].getPhotoId() == photoId && photos[i].getAlbumId() == albumId) {
                    photos[i].setUrl(url);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Photo getPhotoFromAlbum(int photoId, int albumId) {
        for (int i = 0; i < size; i++) {
            if (photos[i].getPhotoId() == photoId && photos[i].getAlbumId() == albumId) {
                return photos[i];
            }
        }
        return null;
    }

    @Override
    public Photo[] getAllPhotoFromAlbum(int albumId) {
        return  getByPredicate(p -> p.getAlbumId() == albumId);
    }

    @Override
    public Photo[] getPhotoBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
        return getByPredicate(p -> p.getDate().toLocalDate().isEqual(dateFrom) ||
                p.getDate().toLocalDate().isAfter(dateFrom) && p.getDate().toLocalDate().isBefore(dateTo)
        );
    }

    private Photo[] getByPredicate(Predicate<Photo> predicate) {
        Photo[] res = new Photo[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(photos[i])) {
                res[j++] = photos[i];
            }
        }
        return Arrays.copyOf(res, j);

    }

    @Override
    public int size() {
        return size;
    }
}
