package ae.gov.sdg.paperless.platform.util;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import ae.gov.sdg.paperless.platform.exceptions.DNRuntimeException;

public final class FileUtil {
	
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
    private FileUtil() {
    }

    private static final String CLASSPATH = "classpath:";
    private static final String DOUBLE_SLASH = "\\";
    private static final String NEW_LINE = "\n";

    public static <T> String readText(final String relativePath, final Class<T> classs) throws IOException {
        try {
            final URL resource = classs.getClassLoader().getResource(relativePath);
            final File file = new File(requireNonNull(resource).getFile());
            final byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes);
        } catch (final IOException e) {
            throw new DNRuntimeException(e);
        }
    }

    public static <T> String fetchFileAsString(final String fileName, final Class<T> classs) throws IOException {

        final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classs.getClassLoader());
        StringBuilder sb;
        try (InputStream in = resolver.getResource(CLASSPATH + DOUBLE_SLASH + fileName).getInputStream()) {
            try (BufferedReader buf = new BufferedReader(new InputStreamReader(in))) {
                String line = buf.readLine();
                sb = new StringBuilder();
                while (line != null) {
                    sb.append(line).append(NEW_LINE);
                    line = buf.readLine();
                }
            }
        }
        return sb.toString();
    }

    public static <T> Stream<Resource> fileList(final String pathPrefix, final Class<T> clazz) throws IOException {
        Stream<Resource> resourceStream = null;
        try {
            final ClassLoader cl = clazz.getClass().getClassLoader();
            final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
            final Resource[] resources = resolver.getResources(pathPrefix);
            resourceStream = Arrays.stream(resources);
        } catch (final FileNotFoundException e) {
        	log.error(e.getMessage());
        }
        return resourceStream;
    }

}
