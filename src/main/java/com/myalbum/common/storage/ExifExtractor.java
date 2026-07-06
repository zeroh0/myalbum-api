package com.myalbum.common.storage;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ExifExtractor {

    public record PhotoExifInfo(
            String cameraMake,
            String cameraModel,
            String lensModel,
            Integer iso,
            String aperture,
            String shutterSpeed,
            Double focalLength,
            LocalDate takenAt
    ) {
    }

    public static PhotoExifInfo extract(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            return extract(is);
        }
    }

    public static PhotoExifInfo extract(byte[] bytes) throws Exception {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return extract(is);
        }
    }

    private static PhotoExifInfo extract(InputStream inputStream) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifSubIFDDirectory subIfd = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        String make = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MAKE) : null;
        String model = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MODEL) : null;

        String lens = null;
        Integer iso = null;
        String aperture = null;
        String shutterSpeed = null;
        Double focalLength = null;
        LocalDate takenAt = null;

        if (subIfd != null) {
            lens = subIfd.getString(ExifSubIFDDirectory.TAG_LENS_MODEL);

            if (subIfd.containsTag(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT)) {
                iso = subIfd.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
            }

            if (subIfd.containsTag(ExifSubIFDDirectory.TAG_FNUMBER)) {
                Double fNumber = subIfd.getDoubleObject(ExifSubIFDDirectory.TAG_FNUMBER);
                aperture = fNumber != null ? String.format("f/%.1f", fNumber) : null;
            }

            if (subIfd.containsTag(ExifSubIFDDirectory.TAG_EXPOSURE_TIME)) {
                shutterSpeed = subIfd.getDescription(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
            }

            if (subIfd.containsTag(ExifSubIFDDirectory.TAG_FOCAL_LENGTH)) {
                focalLength = subIfd.getDoubleObject(ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
            }

            Date dateOriginal = subIfd.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            if (dateOriginal != null) {
                takenAt = dateOriginal.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }
        }

        return new PhotoExifInfo(make, model, lens, iso, aperture, shutterSpeed, focalLength, takenAt);
    }

}
