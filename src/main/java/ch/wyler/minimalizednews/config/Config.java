package ch.wyler.minimalizednews.config;

import ch.wyler.minimalizednews.pojos.TestDataArticle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Class for accessing configurations
 */
@Getter
@Setter
@Component
@Configuration
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "custom")
public class Config {

	@Value("${custom.testmode.enabled}")
	private boolean testModeEnabled;

	private String[] stopwords;
	private Map<String, String> abbreviations;
	private List<TestDataArticle> testdataarticles;
}
