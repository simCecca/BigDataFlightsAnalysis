package bd.flighsAnalysis;

import java.util.Calendar;
import java.util.Date;

import scala.Tuple3;

public class DateHelper {
	
	/**
	 * Returns a {@link Tuple3} representing the date passed as parameter
	 * The values of the tuple are (year, month, day)
	 * @param d a {@link Date}
	 * @return a {@link Tuple3} representing the Date d
	 */
	public static Tuple3<Integer, Integer, Integer> getDateTuple(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH);
		Integer day = cal.get(Calendar.DAY_OF_MONTH);
		
		return new Tuple3<>(year, month, day);
	}

}
