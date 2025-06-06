package ru.sdmitriy612.collection.flat;

import java.io.Serializable;

/**
 * Builder class for Flat class.
 *
 * @see Flat
 * @see Coordinates
 * @see Furnish
 * @see View
 * @see Transport
 * @see House
 */
public class FlatBuilder implements Serializable {
    private String name;
    private Coordinates coordinates;
    private Float area;
    private int numberOfRooms;
    private Furnish furnish;
    private View view;
    private Transport transport;
    private House house;

    /**
     * Builds flat from inputted data.
     * @return Flat instance with inputted properties.
     */
    public Flat build(){
        return new Flat(name, coordinates, area, numberOfRooms, furnish, view, transport, house);
    }

    /**
     * Sets Float's area
     * @param area float value, must be greater than zero
     */
    public void setArea(Float area) {
        if(area <= 0) throw new IllegalArgumentException("area must be greater than zero");
        this.area = area;
    }
    /**
     * Sets Float's area
     * @param name String value, cannot be empty
     */
    public void setName(String name) {
        if(name.isEmpty()) throw new IllegalArgumentException("name cannot be empty");
        this.name = name;
    }

    /**
     * Sets Float's coordinates
     * @param coordinates {@code Coordinates} value
     * @see Coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Sets Float's numberOfRooms
     * @param numberOfRooms int value, must be greater than zero and less than 20.
     */
    public void setNumberOfRooms(int numberOfRooms) {
        if(numberOfRooms <= 0) throw new IllegalArgumentException("number of rooms must be greater than zero");
        if(numberOfRooms > 20) throw new IllegalArgumentException("number of rooms must be less than 20");
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * Sets Float's furnish
     * @param furnish {@code Furnish} value
     * @see Furnish
     */
    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    /**
     * Sets Float's view
     * @param view {@code View} value
     * @see View
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Sets Float's transport
     * @param transport {@code Transport} value
     * @see Transport
     */
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    /**
     * Sets Float's house
     * @param house {@code House} instance
     * @see House
     */
    public void setHouse(House house) {
        this.house = house;
    }
}
