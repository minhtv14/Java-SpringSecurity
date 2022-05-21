package com.security.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService implements IStorageService {

	private final Path storageFolder = Paths.get("src/main/resources/static/uploads");

	public ImageStorageService() {
		try {
			Files.createDirectories(storageFolder);
		} catch (IOException e) {
			throw new RuntimeException("Failed to store empty file");
		}
	}

	private boolean isImageFile(MultipartFile file) {
		String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
		return Arrays.asList(new String[] { "png", "jpg", "jpeg", "bmp" }).contains(fileExtension.trim());
	}

	@Override
	public String storeFile(MultipartFile file) {
		System.out.println(file.getOriginalFilename());
		try {
			System.out.println("haha");
			if (file.isEmpty()) {
				throw new RuntimeException("Failed to store empty file");
			}
			if(!isImageFile(file)) {
				throw new RuntimeException("You can only upload image file");
			}

			float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;

			if (fileSizeInMegabytes > 5.0f) {
				throw new RuntimeException("File must be <= 5Mb");
			}

			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
			System.out.println(fileExtension);
			String generatedfileName = UUID.randomUUID().toString().replace("-", "");
			generatedfileName = generatedfileName + "." + fileExtension;
			Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedfileName)).normalize()
					.toAbsolutePath();
			if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
				throw new RuntimeException("Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
			return generatedfileName;
		} catch (IOException e) {
			throw new RuntimeException("Failed to store", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.storageFolder, 1).filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._")).map(this.storageFolder::relativize);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load stored files", e);
		}
	}

	@Override
	public byte[] readFileContent(String fillName) {
		try {
			Path file = storageFolder.resolve(fillName);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
				return bytes;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new byte[0];
	}

	@Override
	public void deleteAllFiles() {
		// TODO Auto-generated method stub

	}

}
