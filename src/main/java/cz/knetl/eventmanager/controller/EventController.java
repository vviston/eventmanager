package cz.knetl.eventmanager.controller;

import cz.knetl.eventmanager.dto.ApiErrorResponse;
import cz.knetl.eventmanager.dto.EventDto;
import cz.knetl.eventmanager.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API CRUD operations for a Event resource
 */

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = {"available events"})
public class EventController {

	@Autowired
	private EventService eventService;

	@InitBinder
	public void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(       Date.class,
				new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"), true, 10));
	}

	@PostMapping("/event")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a event resource.", notes = "Returns the ID of the new resource in the rowKey header.")
	public void createNewEvent(@RequestBody  @Validated   EventDto event,
							   HttpServletResponse response)  {
		final String rowKey = eventService.create(event);
		response.setHeader("rowKey", rowKey);
	}

	@GetMapping("/event")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get a list of events by month or date.", notes = "You have to provide a valid date in format dd.MM.yyyy. If the filter is set to month api returns events for whole month.")
	public List<EventDto> findEvents(@RequestParam(name = "filter", required = false) @ApiParam(name="filter", required = false, value="Possible values are month/day. Default is day value.") String filter,
									 @RequestParam(name = "date", required = true)  @ApiParam(name="date", required = true, value="The date in the format dd.MM.yyyy")Date date)  {

		List<EventDto> results = null;

		if(filter != null && filter.equals("month")){
			results = eventService.listEventsByMonth(date);
		}else{
			results =  eventService.listEventsByDate(date);
		}

		return results;
	}

	@DeleteMapping("/event")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete a event resource.", notes = "You have to provide a valid rowKey ID in the URL. Once deleted the resource can not be recovered.")
	public void permanentlyDelete(@RequestParam(name = "rowKey", required = true) @ApiParam(name="rowKey", required = true, value="The unique ID of the event") String rowKey)  {
		eventService.permanentlyDelete(rowKey);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler( IllegalArgumentException.class)
	public ApiErrorResponse return400(IllegalArgumentException ex) {
		return new ApiErrorResponse("The parameter is in a bad format. Please refer to the documentation.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler( HttpMessageNotReadableException.class)
	public ApiErrorResponse returnBadRequstJsonBody(HttpMessageNotReadableException ex) {
		return new ApiErrorResponse("JSON request body is in a bad format. Please refer to the documentation.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}