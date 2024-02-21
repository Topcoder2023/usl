package com.gitee.usl.function.base;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author hongda.li, jiahao.song
 */
@FunctionGroup
public class DateFunction {

    @Function("date")
    public LocalDate date(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    @Function("date_value")
    public LocalDate dateValue(String expression, String format) {
        return LocalDate.parse(expression, DateTimeFormatter.ofPattern(format));
    }

    @Function("day")
    public long day(LocalDate date) {
        return date.getDayOfMonth();
    }

    @Function("month")
    public long month(LocalDate date) {
        return date.getMonthValue();
    }

    @Function("now")
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Function("today")
    public LocalDate today() {
        return LocalDate.now();
    }

    @Function("year")
    public long year(LocalDate date) {
        return date.getYear();
    }

    @Function("hour")
    public long hour(LocalDateTime dateTime) {
        return dateTime.getHour();
    }

    @Function("minute")
    public long minute(LocalDateTime dateTime) {
        return dateTime.getMinute();
    }

    @Function("second")
    public long second(LocalDateTime dateTime) {
        return dateTime.getSecond();
    }

    @Function("add_years")
    public Object addYears(Object obj, int num) {
        if (obj instanceof LocalDate) {
            return ((LocalDate)obj).plusYears(num);
        } else {
            return ((LocalDateTime)obj).plusYears(num);
        }
    }

    @Function("add_months")
    public Object addMonths(Object obj, int num) {
        if (obj instanceof LocalDate) {
            return ((LocalDate)obj).plusMonths(num);
        } else {
            return ((LocalDateTime)obj).plusMonths(num);
        }
    }

    @Function("add_weeks")
    public Object plusWeeks(Object obj, int num) {
        if (obj instanceof LocalDate) {
            return ((LocalDate)obj).plusWeeks(num);
        } else {
            return ((LocalDateTime)obj).plusWeeks(num);
        }
    }

    @Function("add_days")
    public Object plusDays(Object obj, int num) {
        if (obj instanceof LocalDate) {
            return ((LocalDate)obj).plusDays(num);
        } else {
            return ((LocalDateTime)obj).plusDays(num);
        }
    }

    @Function("add_hours")
    public LocalDateTime addHours(LocalDateTime dateTime, int num) {
        return dateTime.plusHours(num);
    }

    @Function("add_minutes")
    public LocalDateTime addMinutes(LocalDateTime dateTime, int num) {
        return dateTime.plusMinutes(num);
    }

    @Function("add_seconds")
    public LocalDateTime addSeconds(LocalDateTime dateTime, int num) {
        return dateTime.plusSeconds(num);
    }

    @Function("datetime_value")
    public LocalDateTime datetimeValue(String expression, String format) {
        return LocalDateTime.parse(expression, DateTimeFormatter.ofPattern(format));
    }

    @Function("weekday")
    public long weekday(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

    @Function("date_format")
    public String dateFormat(LocalDate date, String format) {
        return DateTimeFormatter.ofPattern(format).format(date);
    }

    @Function("is_before")
    public long isBefore(LocalDate date1, LocalDate date2) {
        return date2.isBefore(date1)?1:0;
    }

    @Function("is_after")
    public long isAfter(LocalDate date1, LocalDate date2) {
        return date2.isAfter(date1)?1:0;
    }

    @Function("is_not_before")
    public long isNotBefore(LocalDate date1, LocalDate date2) {
        return date2.isBefore(date1)?0:1;
    }

    @Function("is_not_after")
    public long isNotAfter(LocalDate date1, LocalDate date2) {
        return date2.isAfter(date1)?0:1;
    }

    @Function("datediff_year")
    public Object dateDiffYear(Object startTime, Object endTime) {
        if (startTime instanceof LocalDate) {
            return Period.between((LocalDate) startTime, (LocalDate) endTime).getYears();
        } else {
            return ChronoUnit.YEARS.between((LocalDateTime) startTime, (LocalDateTime) endTime);
        }
    }

    @Function("datediff_month")
    public Object dateDiffMonth(Object startTime, Object endTime) {
        if (startTime instanceof LocalDate) {
            return Period.between((LocalDate) startTime, (LocalDate) endTime).getMonths();
        } else {
            return ChronoUnit.MONTHS.between((LocalDateTime) startTime, (LocalDateTime) endTime);
        }
    }

    @Function("datediff_day")
    public Object dateDiffDay(Object startTime, Object endTime) {
        if (startTime instanceof LocalDate) {
            return Period.between((LocalDate) startTime, (LocalDate) endTime).getDays();
        } else {
            return ChronoUnit.DAYS.between((LocalDateTime) startTime, (LocalDateTime) endTime);
        }
    }

    @Function("datediff_hour")
    public long dateDiffHour(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toHours();
    }

    @Function("datediff_minute")
    public long dateDiffMinute(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toMinutes();
    }

    @Function("datediff_second")
    public long dateDiffSecond(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toSeconds();
    }

    @Function("date_of")
    public DateTime date(long timestamp) {
        return DateUtil.date(timestamp);
    }

    @Function("date_timestamp")
    public long timestamp() {
        return System.currentTimeMillis();
    }
}
