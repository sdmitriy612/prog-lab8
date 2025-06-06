package ru.sdmitriy612.collection.flat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sdmitriy612.io.files.FileIOController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Flat class.
 *
 * @see Coordinates
 * @see Furnish
 * @see View
 * @see Transport
 * @see House
 *
 */
public class Flat implements Comparable<Flat>, Serializable {
    private long userOwnerID;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Float area; //Значение поля должно быть больше 0
    private final int numberOfRooms; //Максимальное значение поля: 20, Значение поля должно быть больше 0
    private final Furnish furnish; //Поле может быть null
    private final View view; //Поле может быть null
    private final Transport transport; //Поле может быть null
    private final House house; //Поле может быть null

    /**
     * Public constructor for serializing.
     */
    @JsonCreator
    public Flat(
            @JsonProperty("id") Long id,
            @JsonProperty("userOwnerID") Long userOwnerID,
            @JsonProperty("name") String name,
            @JsonProperty("coordinates") Coordinates coordinates,
            @JsonProperty("creationDate") LocalDateTime creationDate,
            @JsonProperty("area") Float area,
            @JsonProperty("numberOfRooms") int numberOfRooms,
            @JsonProperty("furnish") Furnish furnish,
            @JsonProperty("view") View view,
            @JsonProperty("transport") Transport transport,
            @JsonProperty("house") House house
    ) {
        this.id = id;
        this.name = name;
        this.userOwnerID = userOwnerID;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
    }

    /**
     * Flat's main public constructor
     * @param name
     * @param coordinates
     * @param area
     * @param numberOfRooms
     * @param furnish
     * @param view
     * @param transport
     * @param house
     */
    public Flat(String name, Coordinates coordinates, Float area, int numberOfRooms, Furnish furnish,
                    View view, Transport transport, House house){
        if(name == null) throw new IllegalArgumentException("name cannot be null");
        if(coordinates == null) throw new IllegalArgumentException("coordinates cannot be null");

        this.creationDate = LocalDateTime.now();
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
    }

    public long getUserOwnerID(){
        return userOwnerID;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Float getArea() {
        return area;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public View getView() {
        return view;
    }

    public Transport getTransport() {
        return transport;
    }

    public House getHouse() {
        return house;
    }

    public void setId(long id){
        this.id = id;
    }
    public void setUserOwnerID(long id){this.userOwnerID = id;}
    /**
     * Compares this {@code Flat} with another based on the ratio of area to the number of rooms.
     *
     * @param flat the {@code Flat} to compare with
     * @return a negative number, zero, or a positive number depending on the comparison result
     */
    @Override
    public int compareTo(Flat flat) {
        return Double.compare(this.area, flat.getArea());
    }


    /**
     * Returns a string representation of the {@code Flat} with its details, including ID, name, creation date,
     * coordinates, area, number of rooms, furnish, view, transport, and house (if not null).
     *
     * @return a formatted string with the {@code Flat}'s properties
     */
    @Override
    public String toString() {
        return String.format("""
                Flat
                ID: %d
                Name: %s
                CreationDate: %s
                Coordinates: %s
                Area: %f
                NumberOfRooms: %d
                Furnish: %s
                View: %s
                Transport: %s
                %s
                """, id, name, creationDate, coordinates.toString(), area, numberOfRooms,
                furnish, view, transport, house == null ? "House: null" : house.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return numberOfRooms == flat.numberOfRooms && Objects.equals(id, flat.id) && Objects.equals(name, flat.name) &&
                Objects.equals(coordinates, flat.coordinates) && Objects.equals(creationDate, flat.creationDate)
                && Objects.equals(area, flat.area) && furnish == flat.furnish && view == flat.view
                && transport == flat.transport && Objects.equals(house, flat.house);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, area, numberOfRooms, furnish, view, transport, house);
    }
}