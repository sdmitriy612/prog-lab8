package ru.sdmitriy612.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sdmitriy612.database.DatabaseManager;
import ru.sdmitriy612.interactors.UserAuthorization;

import java.sql.*;

public class User {
    private static final Logger logger = LogManager.getLogger(User.class);
    private long id;
    private final UserAuthorization userAuthorization;
    private boolean isAuthorized = false;

    public User(UserAuthorization userAuthorization){
        this.userAuthorization = userAuthorization;
    }

    public void authorize() throws UserNotFoundException{
        if(userAuthorization == null) throw new UserNotFoundException("Authorization is null");

        String sqlQuery = "SELECT id FROM users WHERE login = ? AND hashed_password = ?";
        try(Connection connection = DatabaseManager.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){

            preparedStatement.setString(1, userAuthorization.login());
            preparedStatement.setString(2, userAuthorization.hashedPassword());

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(!resultSet.next()) throw new UserNotFoundException("User with the provided credentials not found.");

                this.id = resultSet.getInt("id");
                this.isAuthorized = true;

            }

        }catch (UserNotFoundException e){
            throw e;
        }catch (Exception e){
            logger.error("Error during authorization: {}", e.getMessage());
            throw new RuntimeException("Error during authorization: " + e.getMessage(), e);
        }
    }

    public void createUser() {
        String sqlQuery = "INSERT INTO users (login, hashed_password) VALUES (?, ?) RETURNING id";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, userAuthorization.login());
            preparedStatement.setString(2, userAuthorization.hashedPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    this.id = resultSet.getInt("id");
                } else {
                    throw new Exception("User creation failed: no ID returned.");
                }
            }

        }catch (SQLException e){
            if (e.getSQLState().equals("23505")) {
                throw new LoginAlreadyExistsException("Login already exists");
            }
        }catch (Exception e) {
            logger.error("Error during user creation: {}", e.getMessage());
            throw new RuntimeException("Error during user creation: " + e.getMessage(), e);
        }
    }

    public long getId() {
        return id;
    }


    public boolean isAuthorized() {
        return isAuthorized;
    }

    public UserAuthorization getAuthorization() {
        return userAuthorization;
    }
}
