package ru.sdmitriy612.gui.filters;

import ru.sdmitriy612.collection.flat.Flat;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlatFilter {

    private final String id;
    private final String name;
    private final String x;
    private final String y;
    private final String creationDate;
    private final String area;
    private final String rooms;
    private final String furnish;
    private final String view;
    private final String transport;
    private final String houseName;
    private final String houseYear;
    private final String houseLifts;
    private final String ownerId;

    public FlatFilter(String id, String name, String x, String y, String creationDate,
                      String area, String rooms, String furnish, String view,
                      String transport, String houseName, String houseYear, String houseLifts, String ownerId) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
        this.area = area;
        this.rooms = rooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.houseName = houseName;
        this.houseYear = houseYear;
        this.houseLifts = houseLifts;
        this.ownerId = ownerId;
    }

    public List<Flat> filter(List<Flat> flats) {
        return flats.stream()
                .filter(matchesId())
                .filter(matchesName())
                .filter(matchesX())
                .filter(matchesY())
                .filter(matchesCreationDate())
                .filter(matchesArea())
                .filter(matchesRooms())
                .filter(matchesFurnish())
                .filter(matchesView())
                .filter(matchesTransport())
                .filter(matchesHouseName())
                .filter(matchesHouseYear())
                .filter(matchesHouseLifts())
                .filter(matchesOwnerId())
                .collect(Collectors.toList());
    }

    private Predicate<Flat> matchesId() {
        return flat -> id.isEmpty() || String.valueOf(flat.getId()).contains(id);
    }

    private Predicate<Flat> matchesName() {
        return flat -> name.isEmpty() || flat.getName().toLowerCase().contains(name.toLowerCase());
    }

    private Predicate<Flat> matchesX() {
        return flat -> x.isEmpty() || String.valueOf(flat.getCoordinates().x()).contains(x);
    }

    private Predicate<Flat> matchesY() {
        return flat -> y.isEmpty() || String.valueOf(flat.getCoordinates().y()).contains(y);
    }

    private Predicate<Flat> matchesCreationDate() {
        return flat -> creationDate.isEmpty() || flat.getCreationDate().toString().toLowerCase().contains(creationDate.toLowerCase());
    }

    private Predicate<Flat> matchesArea() {
        return flat -> area.isEmpty() || String.valueOf(flat.getArea()).contains(area);
    }

    private Predicate<Flat> matchesRooms() {
        return flat -> rooms.isEmpty() || String.valueOf(flat.getNumberOfRooms()).contains(rooms);
    }

    private Predicate<Flat> matchesFurnish() {
        return flat -> furnish.isEmpty() || (flat.getFurnish() != null &&
                flat.getFurnish().toString().toLowerCase(Locale.ROOT).contains(furnish.toLowerCase()));
    }

    private Predicate<Flat> matchesView() {
        return flat -> view.isEmpty() || (flat.getView() != null &&
                flat.getView().toString().toLowerCase(Locale.ROOT).contains(view.toLowerCase()));
    }

    private Predicate<Flat> matchesTransport() {
        return flat -> transport.isEmpty() || (flat.getTransport() != null &&
                flat.getTransport().toString().toLowerCase(Locale.ROOT).contains(transport.toLowerCase()));
    }

    private Predicate<Flat> matchesHouseName() {
        return flat -> houseName.isEmpty() || (flat.getHouse() != null &&
                flat.getHouse().getName() != null &&
                flat.getHouse().getName().toLowerCase().contains(houseName.toLowerCase()));
    }

    private Predicate<Flat> matchesHouseYear() {
        return flat -> houseYear.isEmpty() || (flat.getHouse() != null &&
                flat.getHouse().getYear() != null &&
                flat.getHouse().getYear().toString().toLowerCase().contains(houseYear.toLowerCase()));
    }

    private Predicate<Flat> matchesHouseLifts() {
        return flat -> houseLifts.isEmpty() || (flat.getHouse() != null &&
                flat.getHouse().getNumberOfLifts() != null &&
                flat.getHouse().getNumberOfLifts().toString().toLowerCase().contains(houseLifts.toLowerCase()));
    }

    private Predicate<Flat> matchesOwnerId() {
        return flat -> ownerId.isEmpty() || String.valueOf(flat.getUserOwnerID()).contains(ownerId);
    }
}
