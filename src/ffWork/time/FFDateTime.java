package ffWork.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class FFDateTime implements Comparable<FFDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final LocalDateTime dateTime;

    private FFDateTime(int year, int month, int day, int hour, int minute) {
        this.dateTime = LocalDateTime.of(year, month, day, hour, minute);
    }

    private FFDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static FFDateTime of(int year, int month, int day, int hour, int minute) {
        return new FFDateTime(year, month, day, hour, minute);
    }

    public static FFDateTime parse(String iso) {
        try {
            return new FFDateTime(LocalDateTime.parse(iso, FORMATTER));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date time: " + iso);
        }
    }

    public int toEpochMinutes() {
        return Math.toIntExact(dateTime.toEpochSecond(ZoneOffset.UTC) / 60);
    }

    public FFDateTime plusMinutes(int minutes) {
        return new FFDateTime(dateTime.plusMinutes(minutes));
    }

    public int minutesUntil(FFDateTime other) {
        return other.toEpochMinutes() - this.toEpochMinutes();
    }

    public int getYear() {
        return dateTime.getYear();
    }

    public int getMonth() {
        return dateTime.getMonthValue();
    }

    public int getDay() {
        return dateTime.getDayOfMonth();
    }

    public static FFDateTime getCurrentDay() {
        LocalDateTime nowDate = LocalDateTime.now();
        return FFDateTime.of(nowDate.getYear(), nowDate.getMonthValue(), nowDate.getDayOfMonth(), nowDate.getHour(), nowDate.getMinute());
    }

    public String getDate() {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    public int compareTo(FFDateTime other) {
        return Integer.compare(this.toEpochMinutes(), other.toEpochMinutes());
    }

    @Override
    public String toString() {
        return dateTime.format(FORMATTER);
    }
}
