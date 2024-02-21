package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Date 函数组单元测试
 *
 * @author jiahao.song
 */
class DateFunctionTest {

    static USLRunner runner = new USLRunner();

    @Test
    void date() {
        Object data = runner.run(new Param("date(2024,2,19)")).getData();
        assertInstanceOf(LocalDate.class, data);
    }

    @Test
    void date_value() {
        Object data = runner.run(new Param("date_value('02-2024-19','MM-yyyy-dd')")).getData();
        assertInstanceOf(LocalDate.class, data);
    }

    @Test
    void day() {
        LocalDate date = LocalDate.parse("2024-02-19");
        Object data = runner.run(new Param("day(date)").addContext("date",date)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void month() {
        LocalDate date = LocalDate.parse("2024-02-19");
        Object data = runner.run(new Param("month(date)").addContext("date",date)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void now() {
        Object data = runner.run(new Param("now()")).getData();
        assertInstanceOf(LocalDateTime.class, data);
    }

    @Test
    void today() {
        Object data = runner.run(new Param("today()")).getData();
        assertInstanceOf(LocalDate.class, data);
    }

    @Test
    void year() {
        LocalDate date = LocalDate.parse("2024-02-19");
        Object data = runner.run(new Param("year(date)").addContext("date",date)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void hour() {
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("hour(dateTime)").addContext("dateTime",dateTime)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void minute() {
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("minute(dateTime)").addContext("dateTime",dateTime)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void second() {
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("second(dateTime)").addContext("dateTime",dateTime)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void add_years() {
        LocalDate date = LocalDate.parse("2024-02-19");
        LocalDateTime dateTime2 = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("add_years(date,1)").addContext("date",date)).getData();
        Object data2 = runner.run(new Param("add_years(dateTime,1)").addContext("dateTime",dateTime2)).getData();
        assertInstanceOf(LocalDate.class, data);
        assertInstanceOf(LocalDateTime.class, data2);
    }

    @Test
    void add_months() {
        LocalDate date = LocalDate.parse("2024-02-19");
        LocalDateTime dateTime2 = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("add_months(date,1)").addContext("date",date)).getData();
        Object data2 = runner.run(new Param("add_months(dateTime,1)").addContext("dateTime",dateTime2)).getData();
        assertInstanceOf(LocalDate.class, data);
        assertInstanceOf(LocalDateTime.class, data2);
    }

    @Test
    void add_weeks() {
        LocalDate date = LocalDate.parse("2024-02-19");
        LocalDateTime dateTime2 = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("add_weeks(date,1)").addContext("date",date)).getData();
        Object data2 = runner.run(new Param("add_weeks(dateTime,1)").addContext("dateTime",dateTime2)).getData();
        assertInstanceOf(LocalDate.class, data);
        assertInstanceOf(LocalDateTime.class, data2);
    }

    @Test
    void add_days() {
        LocalDate date = LocalDate.parse("2024-02-19");
        LocalDateTime dateTime2 = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("add_days(date,1)").addContext("date",date)).getData();
        Object data2 = runner.run(new Param("add_days(dateTime,1)").addContext("dateTime",dateTime2)).getData();
        assertInstanceOf(LocalDate.class, data);
        assertInstanceOf(LocalDateTime.class, data2);
    }

    @Test
    void add_hours() {
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("add_hours(dateTime,1)").addContext("dateTime",dateTime)).getData();
        assertInstanceOf(LocalDateTime.class, data);
    }

    @Test
    void add_minutes() {
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("add_minutes(dateTime,1)").addContext("dateTime",dateTime)).getData();
        assertInstanceOf(LocalDateTime.class, data);
    }

    @Test
    void add_seconds() {
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-19T18:25:30");
        Object data = runner.run(new Param("add_seconds(dateTime,1)").addContext("dateTime",dateTime)).getData();
        assertInstanceOf(LocalDateTime.class, data);
    }

    @Test
    void datetime_value() {
        Object data = runner.run(new Param("datetime_value('2024-02-19 18:25:30','yyyy-MM-dd HH:mm:ss')")).getData();
        assertInstanceOf(LocalDateTime.class, data);
    }

    @Test
    void weekday() {
        LocalDate date = LocalDate.parse("2024-02-19");
        Object data = runner.run(new Param("weekday(date)").addContext("date",date)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void date_format() {
        LocalDate date = LocalDate.parse("2024-02-19");
        Object data = runner.run(new Param("date_format(date,'yyyy-MM-dd')").addContext("date",date)).getData();
        assertInstanceOf(String.class, data);
    }

    @Test
    void is_before() {
        LocalDate date1 = LocalDate.parse("2024-02-19");
        LocalDate date2 = LocalDate.parse("2024-02-18");
        Object data = runner.run(new Param("is_before(date1, date2)").addContext("date1",date1).addContext("date2",date2)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void is_after() {
        LocalDate date1 = LocalDate.parse("2024-02-19");
        LocalDate date2 = LocalDate.parse("2024-02-20");
        Object data = runner.run(new Param("is_after(date1, date2)").addContext("date1",date1).addContext("date2",date2)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void is_not_before() {
        LocalDate date1 = LocalDate.parse("2024-02-19");
        LocalDate date2 = LocalDate.parse("2024-02-18");
        Object data = runner.run(new Param("is_not_before(date1, date2)").addContext("date1",date1).addContext("date2",date2)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void is_not_after() {
        LocalDate date1 = LocalDate.parse("2024-02-19");
        LocalDate date2 = LocalDate.parse("2024-02-20");
        Object data = runner.run(new Param("is_not_after(date1, date2)").addContext("date1",date1).addContext("date2",date2)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void datediff_year() {
        LocalDateTime datetime1 = LocalDateTime.parse("2024-02-19T18:25:30");
        LocalDateTime datetime2 = LocalDateTime.parse("2025-02-19T18:25:30");
        LocalDate date1 = LocalDate.parse("2024-02-19");
        LocalDate date2 = LocalDate.parse("2025-02-19");
        Object data1 = runner.run(new Param("datediff_year(datetime1, datetime2)").addContext("datetime1",datetime1).addContext("datetime2",datetime2)).getData();
        Object data2 = runner.run(new Param("datediff_year(date1, date2)").addContext("date1",date1).addContext("date2",date2)).getData();
        assertInstanceOf(Long.class, data1);
        assertInstanceOf(Long.class, data2);
    }

    @Test
    void datediff_month() {
        LocalDateTime datetime1 = LocalDateTime.parse("2024-02-19T18:25:30");
        LocalDateTime datetime2 = LocalDateTime.parse("2024-03-19T18:25:30");
        LocalDate date1 = LocalDate.parse("2024-02-19");
        LocalDate date2 = LocalDate.parse("2024-03-19");
        Object data1 = runner.run(new Param("datediff_month(datetime1, datetime2)").addContext("datetime1",datetime1).addContext("datetime2",datetime2)).getData();
        Object data2 = runner.run(new Param("datediff_month(date1, date2)").addContext("date1",date1).addContext("date2",date2)).getData();
        assertInstanceOf(Long.class, data1);
        assertInstanceOf(Long.class, data2);
    }

    @Test
    void datediff_day() {
        LocalDateTime datetime1 = LocalDateTime.parse("2024-02-19T18:25:30");
        LocalDateTime datetime2 = LocalDateTime.parse("2024-02-20T18:25:30");
        LocalDate date1 = LocalDate.parse("2024-02-19");
        LocalDate date2 = LocalDate.parse("2024-02-20");
        Object data1 = runner.run(new Param("datediff_day(datetime1, datetime2)").addContext("datetime1",datetime1).addContext("datetime2",datetime2)).getData();
        Object data2 = runner.run(new Param("datediff_day(date1, date2)").addContext("date1",date1).addContext("date2",date2)).getData();
        assertInstanceOf(Long.class, data1);
        assertInstanceOf(Long.class, data2);
    }

    @Test
    void datediff_hour() {
        LocalDateTime datetime1 = LocalDateTime.parse("2024-02-19T18:25:30");
        LocalDateTime datetime2 = LocalDateTime.parse("2024-02-19T23:25:30");
        Object data = runner.run(new Param("datediff_hour(datetime1, datetime2)").addContext("datetime1",datetime1).addContext("datetime2",datetime2)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void datediff_minute() {
        LocalDateTime datetime1 = LocalDateTime.parse("2024-02-19T18:25:30");
        LocalDateTime datetime2 = LocalDateTime.parse("2024-02-19T23:25:30");
        Object data = runner.run(new Param("datediff_minute(datetime1, datetime2)").addContext("datetime1",datetime1).addContext("datetime2",datetime2)).getData();
        assertInstanceOf(Long.class, data);
    }

    @Test
    void datediff_second() {
        LocalDateTime datetime1 = LocalDateTime.parse("2024-02-19T18:25:30");
        LocalDateTime datetime2 = LocalDateTime.parse("2024-02-19T23:25:30");
        Object data = runner.run(new Param("datediff_second(datetime1, datetime2)").addContext("datetime1",datetime1).addContext("datetime2",datetime2)).getData();
        assertInstanceOf(Long.class, data);
    }
}