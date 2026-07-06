package com.myalbum.common.storage;

import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.exception.StorageError;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageDimensionExtractor {

    public record ImageDimension(int width, int height) {
    }

    public static ImageDimension extract(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            return extractFromStream(is);
        }
    }

    public static ImageDimension extract(byte[] bytes) throws IOException {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return extractFromStream(is);
        }
    }

    private static ImageDimension extractFromStream(InputStream is) throws IOException {
        try (ImageInputStream iis = ImageIO.createImageInputStream(is)) {
            if (iis == null) {
                AppException.exception(StorageError.IMAGE_READ_ERROR);
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                AppException.exception(StorageError.UNSUPPORTED_IMAGE_FORMAT);
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(iis);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                return new ImageDimension(width, height);
            } finally {
                reader.dispose();
            }
        }
    }

}
