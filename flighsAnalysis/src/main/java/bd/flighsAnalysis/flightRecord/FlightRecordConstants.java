package bd.flighsAnalysis.flightRecord;


public interface FlightRecordConstants {
	public int YEAR = 0;
	
	public int MONTH = 1;
	
	public int DAY = 2;
	
	public int CARRIER = 3;
	
	public int AIRLINE = 4;
	
	public int TAILNUM = 5;
	
	public int ORIGIN_AIRPORT_ID = 6;
	
	public int ORIGIN_AIRPORT_IATA = 7;
	
	public int ORIGIN_CITY_NAME = 8;
	
	public int ORIGIN_STATE_NAME = 9;
	
	public int DEST_AIRPORT_ID  = 10;
	
	public int DEST_AIRPORT_IATA = 11;
	
	public int DEST_CITY_NAME = 12;
	
	public int DEST_STATE_NAME = 13;
	
	public int DEP_TIME = 14; 
	
	public int DEP_DELAY = 15;
		
	public int ARR_TIME = 16; 
	
	public int ARR_DELAY = 17;
	
	public int CANCELLED = 18;
	
	public int CANCELLATION_CODE = 19;
	
	public int DIVERTED = 20;
	
	public int ELAPSED_TIME = 21;
	
	public int AIRTIME = 22;
	
	public int DISTANCE = 23;
	
	public int DISTANCE_GROUP = 24;
	
	public int CAUSE_CARRIER_DELAY = 25;
	
	public int CAUSE_WEATHER_DELAY = 26;
	
	public int CAUSE_NAS_DELAY = 27;
	
	public int CAUSE_SECURITY_DELAY = 28;
	
	public int CAUSE_LATEAIRCRAFT_DELAY = 29;
	
	public int DIVERTED_LANDINGS = 30;

	public int NUM_OF_FIELDS = 31 + 1; // +1 there is an extra comma at the end of each line (wtf)
	
}
