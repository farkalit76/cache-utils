package ae.gov.sdg.paperless.platform.util;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.FileCopyUtils;

import ae.gov.sdg.paperless.platform.exceptions.ResourceLoaderException;

/**
 * @author c_chandra.bommise
 * 
 * Reads the docker secrets from docker environment variables
 *
 */
public class DockerSecretProcessor implements EnvironmentPostProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(DockerSecretProcessor.class);
	private Stream<Path> list;

	@Override
	public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
		
		// Reads the path of secret from application yaml files configured in docker-secret bind-path
		final String bindPathPpty = environment.getProperty("docker-secret.bind-path");
		
		log.info("value of \"docker-secret.bind-path\" property {}", bindPathPpty);
		
		if (isNotEmpty(bindPathPpty)) {
			final Path bindPath = Paths.get(bindPathPpty);
			if (bindPath.toFile().exists()) {
				Map<String,Object> dockerSecrets;
				try {
					list = Files.list(bindPath);
					dockerSecrets = 
					  list
					    .collect(
					      Collectors.toMap(
					        path -> {
					        	final File secretFile = path.toFile();
					        	return secretFile.getName();
					        },
					        path -> {
					        	final File secretFile = path.toFile();
								try {
									final byte[] content = FileCopyUtils.copyToByteArray(secretFile);
									final StringBuilder finalContent = new StringBuilder(new String(content));
									return finalContent.toString().trim();
								} catch (final IOException e) {
									throw new ResourceLoaderException(e);
								}
					        }
					      ));
				} catch (final IOException e) {
					throw new ResourceLoaderException(e);
				}finally {
					if( list != null) {
						list.close();
					}
				}
				
				final MapPropertySource pptySource = new MapPropertySource("docker-secrets",dockerSecrets);
				
				environment.getPropertySources().addLast(pptySource);
				
			}
		}			
	}

}