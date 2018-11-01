package bd.flighsAnalysis.flightRecord;

import java.util.Date;

public class FlightRecord {
	
	private Date date;
	
	private String carrierId;
	
	private String airlineId;
	
	private String planeTailNum;
	
	private String originAirportId;
	
	private String originAirportIATA;
	
	private String originCityName;
	
	private String originStateName;
	
	private String destAirportId;
	
	private String destAirportIATA;
	
	private String destCityName;
	
	private String destStateName;
	
	private String departureTime; // String??? should this be in date?
	
	private float depDelayMins;
		
	private String arrivalTime; // String??
	
	private float arrDelay;
	
	private boolean cancelled;
	
	private String cancellationCode;
	
	private boolean diverted;
	
	private float totTimeElapsed;
	
	private float airTime;
	
	private float distance;
	
	private int distanceGroup;
	
	private float carrierDelay;
	private float weatherDelay;
	private float nasDelay;
	private float securityDelay;
	private float lateAircraftDelay;
	
	private int divertedAirportLandings;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	public String getAirlineId() {
		return airlineId;
	}

	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}

	public String getPlaneTailNum() {
		return planeTailNum;
	}

	public void setPlaneTailNum(String planeTailNum) {
		this.planeTailNum = planeTailNum;
	}

	public String getOriginAirportId() {
		return originAirportId;
	}

	public void setOriginAirportId(String originAirportId) {
		this.originAirportId = originAirportId;
	}

	public String getOriginAirportIATA() {
		return originAirportIATA;
	}

	public void setOriginAirportIATA(String originAirportIATA) {
		this.originAirportIATA = originAirportIATA;
	}

	public String getOriginCityName() {
		return originCityName;
	}

	public void setOriginCityName(String originCityName) {
		this.originCityName = originCityName;
	}

	public String getOriginStateName() {
		return originStateName;
	}

	public void setOriginStateName(String originStateName) {
		this.originStateName = originStateName;
	}

	public String getDestAirportId() {
		return destAirportId;
	}

	public void setDestAirportId(String destAirportId) {
		this.destAirportId = destAirportId;
	}

	public String getDestAirportIATA() {
		return destAirportIATA;
	}

	public void setDestAirportIATA(String destAirportIATA) {
		this.destAirportIATA = destAirportIATA;
	}

	public String getDestCityName() {
		return destCityName;
	}

	public void setDestCityName(String destCityName) {
		this.destCityName = destCityName;
	}

	public String getDestStateName() {
		return destStateName;
	}

	public void setDestStateName(String destStateName) {
		this.destStateName = destStateName;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public Float getDepDelayMins() {
		return depDelayMins;
	}

	public void setDepDelayMins(float depDelayMins) {
		this.depDelayMins = depDelayMins;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Float getArrDelay() {
		return arrDelay;
	}

	public void setArrDelay(float arrDelay) {
		this.arrDelay = arrDelay;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getCancellationCode() {
		return cancellationCode;
	}

	public void setCancellationCode(String cancellationCode) {
		this.cancellationCode = cancellationCode;
	}

	public boolean isDiverted() {
		return diverted;
	}

	public void setDiverted(boolean diverted) {
		this.diverted = diverted;
	}

	public Float getTotTimeElapsed() {
		return totTimeElapsed;
	}

	public void setTotTimeElapsed(float totTimeElapsed) {
		this.totTimeElapsed = totTimeElapsed;
	}

	public Float getAirTime() {
		return airTime;
	}

	public void setAirTime(float airTime) {
		this.airTime = airTime;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public int getDistanceGroup() {
		return distanceGroup;
	}

	public void setDistanceGroup(int distanceGroup) {
		this.distanceGroup = distanceGroup;
	}

	public Float getCarrierDelay() {
		return carrierDelay;
	}

	public void setCarrierDelay(float carrierDelay) {
		this.carrierDelay = carrierDelay;
	}

	public Float getWeatherDelay() {
		return weatherDelay;
	}

	public void setWeatherDelay(float weatherDelay) {
		this.weatherDelay = weatherDelay;
	}

	public Float getNasDelay() {
		return nasDelay;
	}

	public void setNasDelay(float nasDelay) {
		this.nasDelay = nasDelay;
	}

	public Float getSecurityDelay() {
		return securityDelay;
	}

	public void setSecurityDelay(float securityDelay) {
		this.securityDelay = securityDelay;
	}

	public Float getLateAircraftDelay() {
		return lateAircraftDelay;
	}

	public void setLateAircraftDelay(float lateAircraftDelay) {
		this.lateAircraftDelay = lateAircraftDelay;
	}

	public int getDivertedAirportLandings() {
		return divertedAirportLandings;
	}

	public void setDivertedAirportLandings(int divertedAirportLandings) {
		this.divertedAirportLandings = divertedAirportLandings;
	}

	
}
