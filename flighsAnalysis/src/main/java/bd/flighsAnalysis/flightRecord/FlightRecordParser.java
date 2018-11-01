package bd.flighsAnalysis.flightRecord;

import static bd.flighsAnalysis.flightRecord.FlightRecordConstants.*;

import java.io.Serializable;

import java.util.Date;
import java.util.GregorianCalendar;

import com.opencsv.CSVParser;

import bd.flighsAnalysis.Parser;


public class FlightRecordParser implements Serializable, Parser<FlightRecord> {
		
	private String checkEmpty(String str, String replaceWith) {
		return str.equals("") ? replaceWith : str;
	}

	private static final long serialVersionUID = -3502884135305628623L;

	@Override
	public FlightRecord parseLine(String line) {
		FlightRecord record = new FlightRecord();

		String[] values = null;
		
		try {
			values = new CSVParser().parseLine(line);

			if(values.length != NUM_OF_FIELDS) {
				System.err.println("invalid Line too few/many fields " + values.length);
				return null;
			}

			record.setDate(parseDate(values));
			
			record.setCarrierId(values[CARRIER]);
			
			record.setAirlineId(values[AIRLINE]);
			
			
			record.setPlaneTailNum(values[FlightRecordConstants.TAILNUM]);
			
			parseAirports(record, values);
			
			
			record.setDepDelayMins(Float.parseFloat(checkEmpty(values[DEP_DELAY], "0")));
			
			// TODO: ARR TIME
			
			record.setArrDelay(Float.parseFloat(checkEmpty(values[ARR_DELAY], "0")));
			
			record.setCancelled(values[CANCELLED].startsWith("1"));
			
			record.setCancellationCode(values[CANCELLATION_CODE]);
			
			record.setDiverted(values[DIVERTED].startsWith("1"));
			
			
			record.setTotTimeElapsed(Float.parseFloat(checkEmpty(values[ELAPSED_TIME], "-1")));
			
			record.setAirTime(Float.parseFloat(checkEmpty(values[AIRTIME], "-1")));
			
			record.setDistance(Float.parseFloat(checkEmpty(values[DISTANCE], "-1")));
			
			record.setDistanceGroup(Integer.parseInt(checkEmpty(values[DISTANCE_GROUP], "-1")));
			
			parseDelayCause(record, values);

			record.setDivertedAirportLandings(Integer.parseInt(values[DIVERTED_LANDINGS]));

		} catch (Exception e) {
			System.err.println("Invalid Line " + e.getMessage());
			e.printStackTrace();
			System.err.println(values[DEP_TIME]);
			return null;
		}

		return record;
	}
	
	private void parseDelayCause(FlightRecord record, String[] values) {
		float carrierDelay = Float.parseFloat(checkEmpty(values[CAUSE_CARRIER_DELAY], "0"));
		float weatherDelay = Float.parseFloat(checkEmpty(values[CAUSE_WEATHER_DELAY], "0"));
		float nasDelay = Float.parseFloat(checkEmpty(values[CAUSE_NAS_DELAY], "0"));
		float securityDelay = Float.parseFloat(checkEmpty(values[CAUSE_SECURITY_DELAY], "0"));
		float lateAircraftDelay = Float.parseFloat(checkEmpty(values[CAUSE_LATEAIRCRAFT_DELAY], "0"));
	
		record.setCarrierDelay(carrierDelay);
		record.setWeatherDelay(weatherDelay);
		record.setNasDelay(nasDelay);
		record.setSecurityDelay(securityDelay);
		record.setLateAircraftDelay(lateAircraftDelay);
	}

	private Date parseDate(String[] vals) {
		int year = Integer.parseInt(vals[FlightRecordConstants.YEAR]);
		int month = Integer.parseInt(vals[FlightRecordConstants.MONTH]) - 1; // 0 based
		int day = Integer.parseInt(vals[FlightRecordConstants.DAY]);
		
		int hour = 0;
		int mins = 0;
		try {
			hour = Integer.parseInt(vals[FlightRecordConstants.DEP_TIME].substring(0, 2));
			mins = Integer.parseInt(vals[FlightRecordConstants.DEP_TIME].substring(2, 4));
		}catch (IndexOutOfBoundsException e) {
			// TODO set a flag in the record (invalid hour)
		}
		
		return new GregorianCalendar(year, month, day, hour, mins).getTime();
	}
	
	private void parseAirports(FlightRecord r, String[] vals) {
		r.setOriginAirportId(vals[ORIGIN_AIRPORT_ID]);
		r.setOriginAirportIATA(vals[ORIGIN_AIRPORT_IATA]);
		
		r.setDestAirportId(vals[DEST_AIRPORT_ID]);
		r.setDestAirportIATA(vals[DEST_AIRPORT_IATA]);
		
		r.setOriginCityName(vals[ORIGIN_CITY_NAME]);
		r.setOriginStateName(vals[ORIGIN_STATE_NAME]);
		
		r.setDestCityName(vals[DEST_CITY_NAME]);
		r.setDestStateName(vals[DEST_STATE_NAME]);
	}


}
