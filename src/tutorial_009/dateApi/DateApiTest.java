package tutorial_009.dateApi;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class DateApiTest {

	/*
	 * Java 8 contains a brand new date and time API under the package java.time. 
	 * The new Date API is comparable with the Joda-Time library, however it's not 
	 * the same.
	 */
	public static void main(String[] args) {
		/*
		 * CLOCK & INSTANT: Clock provides access to the current date and time. Clocks are aware of a 
		 * timezone and may be used instead of System.currentTimeMillis() to retrieve the 
		 * current milliseconds. Such an instantaneous point on the time-line is also represented 
		 * by the class Instant. Instants can be used to create legacy java.util.Date objects.
		 */
		Clock clock = Clock.systemDefaultZone();
		long millis = clock.millis();
		System.out.println(millis);

		Instant instant = clock.instant();
		System.out.println(instant); // Output something like "2018-11-29T12:22:50.058Z".
		Date legacyDate = Date.from(instant);   // legacy java.util.Date. 
		System.out.println(legacyDate); // Output something like "Thu Nov 29 13:22:50 CET 2018".
		
		System.out.println("=====================================");
		
		/*
		 * TIMEZONES : Timezones are represented by a ZoneId. They can easily be accessed via static 
		 * factory methods. Timezones define the offsets which are important to convert between instants 
		 * and local dates and times.
		 */
		
		System.out.println(ZoneId.getAvailableZoneIds()); // Output all available timezone ids.

		ZoneId parisZone = ZoneId.of("Europe/Paris");
		ZoneId brazilZone = ZoneId.of("Brazil/East");
		System.out.println(parisZone.getRules()); // ZoneRules[currentStandardOffset=+01:00]
		System.out.println(brazilZone.getRules()); // ZoneRules[currentStandardOffset=-03:00]

		System.out.println("=====================================");
		
		/*
		 * LOCALTIME : LocalTime represents a time without a timezone, e.g. 10pm or 17:30:15. The following 
		 * example creates two local times for the timezones defined above. Then we compare both times and 
		 * calculate the difference in hours and minutes between both times.
		 */
		LocalTime parisNow = LocalTime.now(parisZone);
		LocalTime brazilNow = LocalTime.now(brazilZone);

		System.out.println(parisNow); // Output the now hour in Paris.
		System.out.println(brazilNow); // Output the now hour in Brazil (east).
		
		System.out.println(parisNow.isBefore(brazilNow));  // false

		long hoursBetween = ChronoUnit.HOURS.between(parisNow, brazilNow);
		long minutesBetween = ChronoUnit.MINUTES.between(parisNow, brazilNow);

		System.out.println(hoursBetween);
		System.out.println(minutesBetween);

		/*
		 * LocalTime comes with various factory method to simplify the creation of new instances, 
		 * including parsing of time strings.
		 */
		LocalTime late = LocalTime.of(23, 59, 59);
		System.out.println(late);       // 23:59:59

		DateTimeFormatter frenchTimeFormatter = DateTimeFormatter
										        .ofLocalizedTime(FormatStyle.SHORT)
										        .withLocale(Locale.FRENCH);

		LocalTime leetTime = LocalTime.parse("13:37", frenchTimeFormatter);
		System.out.println(leetTime);   // 13:37
		
		System.out.println("=====================================");
		
		/*
		 * LOCALDATE : LocalDate represents a distinct date, e.g. 2014-03-11. It's immutable and works exactly 
		 * the sale way as LocalTime. The sample demonstrates how to calculate new dates by adding or substracting days, 
		 * months or years. Keep in mind that each manipulation returns a new instance.
		 */
		LocalDate today = LocalDate.now();
		System.out.println(today);
		
		LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
		System.out.println(tomorrow);
		
		LocalDate yesterday = tomorrow.minusDays(2);
		System.out.println(yesterday);

		LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
		System.out.println(independenceDay);
		
		DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
		System.out.println(dayOfWeek); // FRIDAY
		
		/*
		 * Parsing a LocalDate from a string is just as simple as parsing a LocalTime :
		 */
		DateTimeFormatter germanDateFormatter = DateTimeFormatter
											        .ofLocalizedDate(FormatStyle.MEDIUM)
											        .withLocale(Locale.GERMAN);

		LocalDate xmas = LocalDate.parse("24.12.2014", germanDateFormatter);
		System.out.println(xmas);   // 2014-12-24

		System.out.println("=====================================");
		
		/*
		 * LOCALDATETIME : LocalDateTime represents a date-time. It combines date and time as seen in the above 
		 * sections into one instance. LocalDateTime is immutable and works similar to LocalTime and LocalDate.
		 *  We can use methods for retrieving certain fields from a date-time :
		 */
		LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

		dayOfWeek = sylvester.getDayOfWeek();
		System.out.println(dayOfWeek); // WEDNESDAY

		Month month = sylvester.getMonth();
		System.out.println(month); // DECEMBER

		long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
		System.out.println(minuteOfDay); // 1439
		
		/*
		 * With the additional information of a timezone it can be converted to an instant. Instants can easily be 
		 * converted to legacy dates of type java.util.Date.
		 */
		Instant sylvesterInstant = sylvester
					        .atZone(ZoneId.systemDefault())
					        .toInstant();
		System.out.println(sylvesterInstant); // 2014-12-31T22:59:59Z

		Date legacySylvesterDate = Date.from(sylvesterInstant);
		System.out.println(legacySylvesterDate); // Wed Dec 31 23:59:59 CET 2014
		
		/*
		 * Formatting date-times works just like formatting dates or times. Instead of using pre-defined formats 
		 * we can create formatters from custom patterns. Unlike java.text.NumberFormat the new DateTimeFormatter 
		 * is immutable and thread-safe.
		 */
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime parsed = LocalDateTime.parse("2014-11-03 07:13", dateTimeFormatter);
		String string = dateTimeFormatter.format(parsed);
		System.out.println(string); // 2014-11-03 07:13
	}

}
