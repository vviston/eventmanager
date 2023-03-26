package cz.knetl.eventmanager.dao;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.*;
import cz.knetl.eventmanager.bo.Event;
import cz.knetl.eventmanager.dto.EventDto;
import cz.knetl.eventmanager.exception.CRUDException;
import cz.knetl.eventmanager.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CRUD operations for a Event Azure Table Storage
 */
@Component
public class EventDaoAzureTableImpl implements EventDao{

	private final static Logger logger = LoggerFactory.getLogger("EventDaoAzureTableImpl");

	private final String PARTITION_KEY = "PartitionKey";
	private final String ROW_KEY = "RowKey";
	private final String DAY_KEY = "DayKey";

	@Autowired
	private CloudStorageAccount cloudStorageAccount;

	/**
	 *  Save an new Event.
	 *
	 * @Param event - event to be stored
	 * @return Unique ID of an new event
	 * @throws CRUDException if saving new event failed
	 *
	 * */
	public String create(final Event event) throws CRUDException {

		CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();

		try {
			CloudTable cloudTable = tableClient.getTableReference(Event.TABLE_NAME);
			TableOperation insertEvent = TableOperation.insertOrReplace(event);
			TableResult result = cloudTable.execute(insertEvent);
			if(result.getHttpStatusCode() > 199 && result.getHttpStatusCode() < 300){
				logger.info("New event successfully created: " + event.getRowKey());
				return event.getRowKey();
			}
			logger.error("Creating an event error. Http status code: " + result.getHttpStatusCode());
			throw new CRUDException("Http status code: " + result.getHttpStatusCode());
		} catch (URISyntaxException | StorageException e) {
			logger.error("Creating or Updating event error: " + e.getMessage());
			throw new CRUDException();
		}
	}

	/**
	 *  Returns events by a date.
	 *
	 * @Param date - searching parameter
	 * @return Collection of events start that date
	 * @throws CRUDException if listing events failed
	 * */
	public Iterable<Event> findByDate(final Date date) throws CRUDException {

		CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();
		try {
			CloudTable cloudTable = tableClient.getTableReference(Event.TABLE_NAME);

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
			String partitionFilter = TableQuery.generateFilterCondition(
					PARTITION_KEY,
					TableQuery.QueryComparisons.EQUAL,
					dateFormat.format(date)
			);

			SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
			String rowFilter = TableQuery.generateFilterCondition(
					DAY_KEY,
					TableQuery.QueryComparisons.EQUAL,
					dayFormat.format(date));

			String combinedFilter = TableQuery.combineFilters(partitionFilter,
					TableQuery.Operators.AND, rowFilter);



			TableQuery<Event> partitionQuery = TableQuery.from(Event.class)
					.where(combinedFilter);

			List<EventDto> events = new ArrayList<>();
			cloudTable.execute(partitionQuery).forEach(e -> events.add(EventMapper.convert(e)));

			return cloudTable.execute(partitionQuery);

		} catch (URISyntaxException | StorageException e) {
			logger.error("Listing events by date error: " + e.getMessage());
			throw new CRUDException(e.getMessage());
		}


	}

	/**
	 *  Returns events by a month.
	 *
	 *  @Param date - searching parameter. Day is omitted.
	 *  @return List of events
	 *  @throws CRUDException if listing events failed
	 * */
	public Iterable<Event> findByMonth(final Date date) throws CRUDException{

		CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();
		try {
			CloudTable cloudTable = tableClient.getTableReference(Event.TABLE_NAME);

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
			String partitionFilter = TableQuery.generateFilterCondition(
					PARTITION_KEY,
					TableQuery.QueryComparisons.EQUAL,
					dateFormat.format(date)
			);

			TableQuery<Event> partitionQuery = TableQuery.from(Event.class)
					.where(partitionFilter);

			return cloudTable.execute(partitionQuery);

		} catch (URISyntaxException | StorageException e) {
			logger.error("Listing events by date error: " + e.getMessage());
			throw new CRUDException(e.getMessage());
		}
	}

	/**
	 *  Permanently delete an Event
	 *
	 * @Param rowKey - Unique event ID
	 * @return Integer http status of operation
	 * @throws CRUDException if removing event failed
	 * */
	public void permanentlyDelete(final String rowKey) throws CRUDException {

		CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();

		try {
			CloudTable cloudTable = tableClient.getTableReference(Event.TABLE_NAME);

			String rowFilter = TableQuery.generateFilterCondition(
					ROW_KEY,
					TableQuery.QueryComparisons.EQUAL,
					rowKey
			);

			TableQuery<Event> rowQuery = TableQuery.from(Event.class)
					.where(rowFilter);

			Iterable<Event> eventsToRemove =  cloudTable.execute(rowQuery);
			for(Event event : eventsToRemove){
				TableOperation deleteEvent = TableOperation.delete(event);
				final TableResult result =  cloudTable.execute(deleteEvent);
				if(result.getHttpStatusCode() < 200 && result.getHttpStatusCode() > 299){
					throw new CRUDException("Http status code: " + result.getHttpStatusCode());
				}
			}

		} catch (URISyntaxException | StorageException e) {
			logger.error("Removing event by date error: " + e.getMessage());
			throw new CRUDException(e.getMessage());
		}
	}
}
