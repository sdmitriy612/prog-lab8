package ru.sdmitriy612.collection.flat;

import java.io.Serializable;
import java.util.Objects;

/**
 * House class.
 */
public class House implements Serializable {
    private String name; //Поле не может быть null
    private Long year; //Значение поля должно быть больше 0
    private Long numberOfLifts; //Значение поля должно быть больше 0

    public String getName() {
        return name;
    }

    public Long getYear() {
        return year;
    }

    public Long getNumberOfLifts() {
        return numberOfLifts;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the year of the house.
     *
     * @param year the year to set
     * @throws IllegalArgumentException if the year is less than or equal to zero
     */
    public void setYear(Long year) {
        if(year <= 0) throw new IllegalArgumentException("house year must be greater than zero.");
        this.year = year;
    }

    /**
     * Sets the number of lifts in the house.
     *
     * @param numberOfLifts the number of lifts to set
     * @throws IllegalArgumentException if the number of lifts is less than or equal to zero
     */
    public void setNumberOfLifts(Long numberOfLifts) {
        if(numberOfLifts <= 0) throw new IllegalArgumentException("house's numberOfLifts must be greater than zero.");
        this.numberOfLifts = numberOfLifts;
    }

    /**
     * Returns a string representation of the {@code House} with its name, year, and number of lifts.
     *
     * @return a formatted string with the house's details
     */
    @Override
    public String toString(){
        return String.format("""
                HouseName: %s
                HouseYear: %d
                HouseNumberOfLifts: %d
                """, name, year, numberOfLifts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return Objects.equals(name, house.name) && Objects.equals(year, house.year)
                && Objects.equals(numberOfLifts, house.numberOfLifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, numberOfLifts);
    }
}