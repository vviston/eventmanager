package cz.knetl.eventmanager.config;

import com.microsoft.azure.storage.CloudStorageAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
public class AzureTableStorageConfig {

	@Value("${ms.azure.table.connection}")
	private String tableConnectionString;

	@Bean
	public CloudStorageAccount getCloudStorageAccount() throws URISyntaxException, InvalidKeyException {
			return CloudStorageAccount.parse(tableConnectionString);
	}
}
