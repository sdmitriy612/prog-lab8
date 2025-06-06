package ru.sdmitriy612.clientapp.handling;

import ru.sdmitriy612.collection.flat.*;
import ru.sdmitriy612.collection.flat.FlatBuilder;
import ru.sdmitriy612.io.StringIOController;

import java.io.IOException;
import java.util.Arrays;
/**
 * This class is responsible for collecting input data to create a {@link Flat} object.
 * It interacts with the user through the {@link StringIOController} to obtain various properties
 * of the flat.
 */
public class ElementInput {
    private final FlatBuilder flatBuilder = new FlatBuilder();
    private final House house = new House();
    private StringIOController stringIOController;
    /**
     * Collects the necessary data from the user to create a new flat.
     * The method interacts with the user to input data for the flat's attributes.
     *
     * @param stringIOController the controller for reading and writing user input/output
     * @return the created {@link FlatBuilder} object
     */
    public FlatBuilder inputFlat(StringIOController stringIOController){
        this.stringIOController = stringIOController;
        inputArea();
        inputCoordinates();
        inputName();
        inputNumberOfRooms();
        inputFurnish();
        inputTransport();
        inputView();
        inputHouse();
        return this.flatBuilder;
    }
    /**
     * Prompts the user to input the flat's area.
     */
    private void inputArea(){
        String line;
        try{
            while ((line = stringIOController.read("Enter flat's area: ")) != null){
                try {
                    flatBuilder.setArea(Float.valueOf(line));
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the flat's name.
     */
    private void inputName() {
        String line;
        try{
            while ((line = stringIOController.read("Enter flat's name: ")) != null){
                try {
                    flatBuilder.setName(line);
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the flat's coordinates in the format "x y".
     */
    private void inputCoordinates() {
        String line;
        try{
            while ((line = stringIOController.read("Enter flat's coordinates with gap (example: 'x y') ")) != null){
                try {
                    String[] coords = line.split(" +");
                    if(coords.length != 2) throw new IllegalArgumentException("coordinates must be inputted in format 'x y'. \nExample: 15 11");
                    Coordinates coordinates = new Coordinates(Double.parseDouble(coords[0]), Long.parseLong(coords[1]));
                    flatBuilder.setCoordinates(coordinates);
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the flat's number of rooms.
     */
    private void inputNumberOfRooms() {
        String line;
        try{
            while ((line = stringIOController.read("Enter flat's numberOfRooms: ")) != null){
                try {
                    flatBuilder.setNumberOfRooms(Integer.parseInt(line));
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the flat's furnish type.
     */
    private void inputFurnish() {
        String line;
        String furnishValues = Arrays.toString(Furnish.values());
        try{
            while ((line = stringIOController.read("Enter flat's furnish from values: " + furnishValues + "\n")) != null){
                try {
                    if(line.isBlank()) flatBuilder.setFurnish(null);
                    else flatBuilder.setFurnish(Furnish.valueOf(line));
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the flat's view type.
     */
    private void inputView() {
        String line;
        String viewValues = Arrays.toString(View.values());
        try{
            while ((line = stringIOController.read("Enter flat's view from values: " + viewValues + "\n")) != null){
                try {
                    if(line.isBlank()) flatBuilder.setView(null);
                    else flatBuilder.setView(View.valueOf(line));
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the flat's transport type.
     */
    private void inputTransport() {
        String line;
        String transportValues = Arrays.toString(Transport.values());
        try{
            while ((line = stringIOController.read("Enter flat's view from values: " + transportValues + "\n")) != null){
                try {
                    if(line.isBlank()) flatBuilder.setTransport(null);
                    else flatBuilder.setTransport(Transport.valueOf(line));
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the associated house information.
     */
    private void inputHouse()  {
        inputHouseName();
        if(house.getName() == null) return;
        inputHouseYear();
        inputHouseNumberOfLifts();
        flatBuilder.setHouse(house);
    }
    /**
     * Prompts the user to input the house's name.
     */
    private void inputHouseName() {
        String line;
        try{
            while ((line = stringIOController.read("Enter house's name: ")) != null){
                try {
                    if(line.isBlank()) house.setName(null);
                    else house.setName(line);
                }catch (IllegalArgumentException e){
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the house's year.
     */
    private void inputHouseYear() {
        String line;
        try {
            while ((line = stringIOController.read("Enter house's year: ")) != null) {
                try {
                    house.setYear(Long.parseLong(line));
                } catch (IllegalArgumentException e) {
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }
    /**
     * Prompts the user to input the house's number of lifts.
     */
    private void inputHouseNumberOfLifts() {
        String line;
        try {
            while ((line = stringIOController.read("Enter house's numberOfLifts: ")) != null) {
                try {
                    house.setNumberOfLifts(Long.parseLong(line));
                } catch (IllegalArgumentException e) {
                    stringIOController.error("Invalid argument: " + e.getMessage());
                    continue;
                }
                break;
            }
        }catch (IOException e){
            stringIOController.error("Input error: " + e.getMessage());
        }
    }



}
