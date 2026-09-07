package com.IDM3.CRUDmuse.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ThumbnailService {

    public void generateThumbnailProject(File inputFile, File outputFile) throws IOException {
        // Generate thumbnail (e.g., 150x150)
        Thumbnails.of(inputFile)
                .forceSize(200, 150)
                .toFile(outputFile);
    }

    public void  generateThumbnailShowcase(File inputFile, File outputFile) throws IOException {
        // Generate thumbnail (e.g., 150x150)
        Thumbnails.of(inputFile)
                .forceSize(1000, 400)
                .toFile(outputFile);
    }

    public void  generateThumbnailUserImage(File inputFile, File outputFile) throws IOException {
        // Generate thumbnail (e.g., 150x150)
        Thumbnails.of(inputFile)
                .forceSize(200, 200)
                .toFile(outputFile);
    }
}
