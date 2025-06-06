package ru.sdmitriy612.collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sdmitriy612.collection.flat.*;
import ru.sdmitriy612.io.formatters.Formatter;
import ru.sdmitriy612.io.formatters.XMLFormatter;
import ru.sdmitriy612.database.DatabaseManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Manages the collection of {@link Flat} objects.
 * <p>
 * This class is implemented as a singleton and provides methods to load and save
 * a collection of persons from and to a file, as well as access the current list
 * of persons in memory.
 * </p>
 *
 * @see Flat
 * @see Formatter
 */
public class CollectionManager {
    private static CollectionManager instance;
    private static final Logger logger = LogManager.getLogger(CollectionManager.class);

    /**
     * Flat HashSet collection.
     */
    private static Set<Flat> collection = ConcurrentHashMap.newKeySet();
    /**
     * Collection creation date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected LocalDateTime creationDate;

    /**
     * Private constructor for serializer.
     */
    @JsonCreator
    private CollectionManager(@JsonProperty("creationDate") LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Private constructor for getInstance
     */
    private CollectionManager(){
        load();
    }

    /**
     * Collection getter.
     * @return collection of flats
     */
    public Set<Flat> getCollection(){
        return collection;
    }

    /**
     * Returns the singleton instance of {@code CollectionManager}.
     * <p>
     * If the instance is {@code null}, a new instance is created and returned.
     * </p>
     *
     * @return the {@code CollectionManager} instance
     */
    public static CollectionManager getInstance(){
        return instance == null ? instance = new CollectionManager() : instance;
    }

    public boolean addElement(Flat flat, long userID){
        String sqlQuery = """
            INSERT INTO flat_collection (
                user_id,
                name,
                coordinate_x,
                coordinate_y,
                area,
                number_of_rooms,
                furnish,
                flat_view,
                transport,
                house_name,
                house_year,
                house_number_of_lifts
            ) VALUES (?, ?, ?, ?, ?, ?, ?::furnish, ?::view_enum, ?::transport, ?, ?, ?)
        """;

        try(
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
        ){

            stmt.setLong(1, userID);
            stmt.setString(2, flat.getName());
            stmt.setDouble(3, flat.getCoordinates().x());
            stmt.setLong(4, flat.getCoordinates().y());
            stmt.setFloat(5, flat.getArea());
            stmt.setInt(6, flat.getNumberOfRooms());

            if (flat.getFurnish() != null) stmt.setString(7, flat.getFurnish().name());
            else stmt.setNull(7, Types.VARCHAR);

            if (flat.getView() != null) stmt.setString(8, flat.getView().name());
            else stmt.setNull(8, Types.VARCHAR);

            if (flat.getTransport() != null) stmt.setString(9, flat.getTransport().name());
            else stmt.setNull(9, Types.VARCHAR);

            if (flat.getHouse() != null){
                stmt.setString(10, flat.getHouse().getName());
                stmt.setLong(11, flat.getHouse().getYear());
                stmt.setLong(12, flat.getHouse().getNumberOfLifts());
            }else{
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.BIGINT);
                stmt.setNull(12, Types.BIGINT);
            }


            stmt.executeUpdate();
            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int flatID = resultSet.getInt("id");
                    flat.setId(flatID);
                    flat.setUserOwnerID(userID);
                }else return false;
            }

            collection.add(flat);
            return true;

        }catch (Exception e){
            logger.error("Error in add element: {}", e.getMessage());
            return false;
        }
    }

    public boolean removeElement(Flat flat, long userID){
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM flat_collection WHERE id = ? AND user_id = ?")){

            preparedStatement.setLong(1, flat.getId());
            preparedStatement.setLong(2, userID);

            int deletedRows = preparedStatement.executeUpdate();

            if(deletedRows > 0){
                collection.remove(flat);
                logger.debug("Element with id {} was removed by user #{}", flat.getId(), userID);
                return true;
            }return false;

        }catch (Exception e){
            logger.error("Error in remove element: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Loads the collection from a file specified by the {@code COLLECTION_FILE} environment variable.
     * <p>
     * If the collection file is not found or the file is empty, a new collection is created with the current date.
     * </p>
     *
     * @throws RuntimeException if the environment variable {@code COLLECTION_FILE} is not set
     */
    public void load() {
        try (
                Connection connection = DatabaseManager.getConnection();
                Statement stmt1 = connection.createStatement();
                ResultSet flatRs = stmt1.executeQuery("SELECT * FROM flat_collection");
                Statement stmt2 = connection.createStatement();
                ResultSet infoResult = stmt2.executeQuery("SELECT creation_date FROM collection_info")
        ) {

            while (flatRs.next()) {
                House house = null;
                String houseName = flatRs.getString("house_name");
                if (!flatRs.wasNull()) {
                    house = new House();
                    house.setName(houseName);
                    house.setYear(flatRs.getLong("house_year"));
                    house.setNumberOfLifts(flatRs.getLong("house_number_of_lifts"));
                }

                String furnishString = flatRs.getString("furnish");
                String viewString = flatRs.getString("flat_view");
                String transportString = flatRs.getString("transport");

                Flat flat = new Flat(
                        flatRs.getLong("id"),
                        flatRs.getLong("user_id"),
                        flatRs.getString("name"),
                        new Coordinates(
                                flatRs.getDouble("coordinate_x"),
                                flatRs.getLong("coordinate_y")
                        ),
                        flatRs.getTimestamp("creation_date").toLocalDateTime(),
                        flatRs.getFloat("area"),
                        flatRs.getInt("number_of_rooms"),
                        furnishString != null ? Furnish.valueOf(furnishString) : null,
                        viewString != null ? View.valueOf(viewString) : null,
                        transportString != null ? Transport.valueOf(transportString) : null,
                        house
                );
                collection.add(flat);
            }

            if (infoResult.next()) {
                creationDate = infoResult.getTimestamp("creation_date").toLocalDateTime();
            } else {
                creationDate = LocalDateTime.now();
                try (Statement statement = connection.createStatement()) {
                    statement.executeQuery("INSERT INTO collection_info (creation_date) VALUES ('" + creationDate + "')");
                }
            }
        } catch (Exception e) {
            logger.error("Error in load collection: {}", e.getMessage());
        }
    }

    /**
     * Saves the current {@code CollectionManager} instance to a file specified by the {@code COLLECTION_FILE} environment variable.
     * <p>
     * If an error occurs during the saving process, it is silently ignored.
     * </p>
     *
     * @throws RuntimeException if the environment variable {@code COLLECTION_FILE} is not set
     */
    public void save(){

        String collectionFilePath = System.getenv("COLLECTION_FILE") == null ?
                "collection.xml" : System.getenv("COLLECTION_FILE");

        try(Formatter<CollectionManager> xmlSerializator = new XMLFormatter(collectionFilePath)){
            xmlSerializator.safeWrite(this);
        }catch (Exception ignored){

        }
    }

    /**
     * Returns a string representation of the collection with CollectionManager creation date and size of collection.
     *
     * @return a formatted string with the creation date and element count
     */
    public String toString(){
        return String.format("""
                Collection Set<Flat>
                CreationDate: %s
                Amount of elements: %d
                """, creationDate, collection.size());
    }
}
