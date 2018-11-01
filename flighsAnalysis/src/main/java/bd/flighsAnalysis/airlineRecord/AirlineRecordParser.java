package bd.flighsAnalysis.airlineRecord;

import static bd.flighsAnalysis.airlineRecord.AirlineRecordConstants.*;

import java.io.Serializable;

import com.opencsv.CSVParser;

import bd.flighsAnalysis.Parser;

public class AirlineRecordParser implements Parser<AirlineRecord>, Serializable {


	private static final long serialVersionUID = -989353327663096684L;

	@Override
	public AirlineRecord parseLine(String line) {
		AirlineRecord record = new AirlineRecord();
		
		String[] values = null;
		
		try {
			values = new CSVParser().parseLine(line);

			if(values.length != NUM_OF_FIELDS) {
				System.err.println("Airline parser invalid Line: too few/many fields " + values.length);
				return null;
			}

			record.setAirlineId(values[AIRLINE_ID]);
			
			record.setAirlineName(values[AIRLINE_NAME]);
			
			record.setLowCost(Boolean.parseBoolean(values[IS_LOW_COST]));
			

		} catch (Exception e) {
			System.err.println("Airline parser Invalid Line " + e.getMessage());
			return null;
		}

		return record;
	}

	
	
}
