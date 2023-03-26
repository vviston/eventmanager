package cz.knetl.eventmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import java.time.format.DateTimeFormatter;

/**
 *  The Event Manager Application
 * */

@SpringBootApplication
public class EventmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmanagerApplication.class, args);
	}

	@Bean
	public FormattingConversionService conversionService() {
		DefaultFormattingConversionService conversionService =
				new DefaultFormattingConversionService(false);

		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		registrar.registerFormatters(conversionService);

		return conversionService;
	}
}
