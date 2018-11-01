package bd.flighsAnalysis.weatherRecord;

import static bd.flighsAnalysis.weatherRecord.WeatherRecordConstants.*;

import java.io.Serializable;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.Range;

import com.opencsv.CSVParser;

import bd.flighsAnalysis.Parser;


public class WeatherRecordParser implements Serializable, Parser<WeatherRecord> {
	

	private static final long serialVersionUID = 1L;
	
	private float toFloat(String val) {
		float res = 0;
		try {
			res = Float.parseFloat(val);
		}catch (NumberFormatException e) {
			
		}
		return res;
	}

	private Range<Integer> getRange(double value, int rangeFactor) {
		int min = ((int)value) / rangeFactor;
		int max = (min + 1) * rangeFactor;
		return Range.between(min * rangeFactor, max);
	}
		

	@Override
	public WeatherRecord parseLine(String line) {
		WeatherRecord record = new WeatherRecord();

		String[] values = null;
		
		try {
			values = new CSVParser().parseLine(line);

			if(values.length != NUM_OF_FIELDS) {
				System.err.println("Weather parser invalid Line: too few/many fields " + values.length);
				return null;
			}

			record.setDate(parseDate(values));
			
			record.setEvents(values[EVENTS]); // TODO: need clean up
			
			
			record.setPrecipitation(getRange(toFloat(values[PRECIPITATION_mm]), 10));
			
			record.setTemperature(getRange(toFloat(values[MIN_TEMP]), 15));
			
			record.setWind(getRange(toFloat(values[MAX_WIND]), 50));
			
			

		} catch (Exception e) {
			System.err.println("Weather parser Invalid Line " + e.getMessage());
			return null;
		}

		return record;
	}
	
	

	private Date parseDate(String[] vals) {
		int year = Integer.parseInt(vals[YEAR]);
		int month = Integer.parseInt(vals[MONTH]) - 1; // 0 based
		int day = Integer.parseInt(vals[DAY]);
		
		
		return new GregorianCalendar(year, month, day).getTime();
	}
	
	

}
