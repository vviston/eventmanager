package cz.knetl.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.charset.Charset;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTests {


	@Autowired
	private MockMvc mockMvc;

	@Test
	public void whenPostRequestToEventsAndValidEvent_thenCorrectResponse() throws Exception {
		MediaType json = new MediaType(MediaType.APPLICATION_JSON, Charset.forName("UTF-8"));
		String event = "{\"name\": \"Test event\", \"text\" : \"Description of the event\", \"start\" : \"10.10.2020 12:00:00\", \"end\" : \"10.10.2020 13:00:00\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/event")
				.content(event)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				;
	}

	@Test
	public void whenPostRequestToEventsAndInValidEvent_thenErrorResponse() throws Exception {
		MediaType json = new MediaType(MediaType.APPLICATION_JSON, Charset.forName("UTF-8"));
		String event = "{\"name\": \"\", \"text\" : \"Description of the event\", \"start\" : \".10.2020 12:00:00\", \"end\" : \"10.10.2020 13:00:00\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/event")
				.content(event)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				;
	}

	@Test
	public void whenGetRequestToEventsAndValidDateParam_thenListOfEventsResponse() throws Exception {
		MediaType json = new MediaType(MediaType.APPLICATION_JSON, Charset.forName("UTF-8"));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event?filter=month&date=10.10.2020"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentType(MediaType.APPLICATION_JSON));
		;
	}

}
