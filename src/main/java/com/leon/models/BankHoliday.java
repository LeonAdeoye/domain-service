package com.leon.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.Objects;

@Document(collection = "bankHolidays")
public class BankHoliday
{
    @Id
    private String id;
    private String countryCode;
    private String holidayName;
    private LocalDate holidayDate;
    private boolean isPublicHoliday;
    private String description;

    public BankHoliday()
    {
    }

    public BankHoliday(String countryCode, String holidayName, LocalDate holidayDate, boolean isPublicHoliday, String description)
    {
        this.countryCode = countryCode;
        this.holidayName = holidayName;
        this.holidayDate = holidayDate;
        this.isPublicHoliday = isPublicHoliday;
        this.description = description;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getHolidayName()
    {
        return holidayName;
    }

    public void setHolidayName(String holidayName)
    {
        this.holidayName = holidayName;
    }

    public LocalDate getHolidayDate()
    {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate)
    {
        this.holidayDate = holidayDate;
    }

    public boolean isPublicHoliday()
    {
        return isPublicHoliday;
    }

    public void setPublicHoliday(boolean publicHoliday)
    {
        isPublicHoliday = publicHoliday;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankHoliday that = (BankHoliday) o;
        return isPublicHoliday == that.isPublicHoliday &&
               Objects.equals(id, that.id) &&
               Objects.equals(countryCode, that.countryCode) &&
               Objects.equals(holidayName, that.holidayName) &&
               Objects.equals(holidayDate, that.holidayDate) &&
               Objects.equals(description, that.description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, countryCode, holidayName, holidayDate, isPublicHoliday, description);
    }

    @Override
    public String toString()
    {
        return "BankHoliday{" +
               "id='" + id + '\'' +
               ", countryCode='" + countryCode + '\'' +
               ", holidayName='" + holidayName + '\'' +
               ", holidayDate=" + holidayDate +
               ", isPublicHoliday=" + isPublicHoliday +
               ", description='" + description + '\'' +
               '}';
    }
}
