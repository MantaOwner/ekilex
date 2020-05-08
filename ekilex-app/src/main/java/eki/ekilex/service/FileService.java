package eki.ekilex.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileService.class);

	@Value("${file.repository.path:}")
	private String fileRepositoryPath;

	public Resource getFileAsResource(String fileName) {

		Path pathToFile = getFilePath(fileName);
		return pathToFile == null ? null : new FileSystemResource(pathToFile);
	}

	private Path getFilePath(String fileName) {

		Path filePath = null;
		try (Stream<Path> dirStream = Files.find(
				Paths.get(fileRepositoryPath),
				4,
				(path, attr) -> attr.isRegularFile() && StringUtils.equals(path.getFileName().toString(), fileName))) {
			Optional<Path> fileToServe = dirStream.findFirst();
			if (fileToServe.isPresent()) {
				filePath = fileToServe.get();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return filePath;
	}

}
