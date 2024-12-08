package org.example.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();
        assert originalFilename != null;
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filepath = path + File.separator +  fileName;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        Files.copy(image.getInputStream(), Paths.get(filepath));
        return fileName;
    }
}
