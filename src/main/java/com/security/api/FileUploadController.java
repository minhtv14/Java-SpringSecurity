package com.security.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.security.models.ResponseObject;
import com.security.service.IStorageService;

@Controller
@RequestMapping("/api/uploads")
public class FileUploadController {

	@Autowired
	private IStorageService storageService;

	@PostMapping("")
	public ResponseEntity<ResponseObject> uploadFile(@RequestParam MultipartFile file) {
		try {
			String generatedFileName = storageService.storeFile(file);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("Ok", "Upload file successfully", generatedFileName));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Ok", e.getMessage(), ""));
		}
	}

	@GetMapping("/files/{fileName:.+}")
	public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
		try {
			byte[] bytes = storageService.readFileContent(fileName);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);

		} catch (Exception e) {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("")
	public ResponseEntity<ResponseObject> getUploadFiles() {
		try {
			List<String> urls = storageService.loadAll().map(path -> {
				String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "readDetailFile", path.getFileName().toString()).build().toUri().toString();
				return urlPath;
			}).collect(Collectors.toList());
			return ResponseEntity.ok(new ResponseObject("Ok", "List files successfully", urls));
		} catch (Exception e) {
			return ResponseEntity.ok(new ResponseObject("Failed", "List files failed", ""));
		}
	}
}
