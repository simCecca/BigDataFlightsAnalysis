package bd.flighsAnalysis.weatherRecord;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.Range;


public class WeatherRecord implements Serializable {
	

	private static final long serialVersionUID = -1265884788480674138L;
	
	
	private Date date;
	
	private Range<Integer> temperature;
	
	private Range<Integer> wind;
	
	private Range<Integer> precipitation;
	
	private String events;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Range<Integer> getTemperature() {
		return temperature;
	}

	public void setTemperature(Range<Integer> temperature) {
		this.temperature = temperature;
	}

	public Range<Integer> getWind() {
		return wind;
	}

	public void setWind(Range<Integer> wind) {
		this.wind = wind;
	}

	public Range<Integer> getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(Range<Integer> precipitation) {
		this.precipitation = precipitation;
	}

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((precipitation == null) ? 0 : precipitation.hashCode());
		result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
		result = prime * result + ((wind == null) ? 0 : wind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeatherRecord other = (WeatherRecord) obj;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		if (precipitation == null) {
			if (other.precipitation != null)
				return false;
		} else if (!precipitation.equals(other.precipitation))
			return false;
		if (temperature == null) {
			if (other.temperature != null)
				return false;
		} else if (!temperature.equals(other.temperature))
			return false;
		if (wind == null) {
			if (other.wind != null)
				return false;
		} else if (!wind.equals(other.wind))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[temperature(C)=" + temperature + ", wind(km/h)=" + wind + ", precipitation(mm)=" + precipitation
				+ ", events=" + events + "]";
	}
	
	
	
	
}
