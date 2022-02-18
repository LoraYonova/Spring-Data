import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JavaDBAppsExercise {
    public static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "minions_db";
    public static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static Connection connection;
    public static PreparedStatement preparedStatement;
    public static ResultSet resultSet;


    public static void main(String[] args) throws SQLException, IOException {

        connection = getConnection();

        System.out.println("Enter ex num:");
        int exNum = Integer.parseInt(reader.readLine());

        switch (exNum) {
            case 2 -> exTwo();
            case 3 -> exThree();
            case 4 -> exFour();
            case 5 -> exFive();
            case 6 -> exSix();
            case 7 -> exSeven();
            case 8 -> exEight();
            case 9 -> exNine();

        }

    }

    private static void exTwo() throws SQLException, IOException {
        preparedStatement = connection.prepareStatement(
                "SELECT v.name, COUNT(DISTINCT minion_id) AS count " +
                        "FROM villains AS v\n" +
                        "JOIN minions_villains mv on v.id = mv.villain_id\n" +
                        "GROUP BY name\n" +
                        "HAVING count > ?;");


        System.out.println("Enter more than");
        int count = Integer.parseInt(reader.readLine());

        preparedStatement.setInt(1, count);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d %n", resultSet.getString("name"),
                    resultSet.getInt("count"));
        }
    }

    private static void exThree() throws SQLException, IOException {

        System.out.println("Enter villain id");
        int villainId = Integer.parseInt(reader.readLine());

        String name = checkIfEntityExist(villainId, "villains", "id");

        if (name == null) {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
            return;
        }


        System.out.printf("Villain: %s %n", name);

        preparedStatement = connection.prepareStatement(
                "SELECT CONCAT(minions.name, ' ', minions.age) AS `full_info` FROM minions\n" +
                        "JOIN minions_villains mv on minions.id = mv.minion_id\n" +
                        "JOIN villains v on v.id = mv.villain_id\n" +
                        "WHERE v.id = ?");
        preparedStatement.setInt(1, villainId);

        resultSet = preparedStatement.executeQuery();
        int count = 1;
        while (resultSet.next()) {
            System.out.printf("%d. %s %n", count++, resultSet.getString("full_info"));
        }

    }

    private static void exFour() throws IOException, SQLException {
        System.out.println("Enter minions:");
        String[] input = reader.readLine().split(": ");
        String[] minionsParam = input[1].split("\\s+");
        String name = minionsParam[0];
        int age = Integer.parseInt(minionsParam[1]);
        String town = minionsParam[2];

        System.out.println("Enter villain name");
        String[] villain = reader.readLine().split(": ");
        String villainName = villain[1];

        String townName = checkEntityExistByName(town, "towns", "name");

        if (townName == null) {
            addData("towns", "country", town, "NULL");
            System.out.printf("Town %s was added to the database.%n", town);
        }

        String nameVillain = checkEntityExistByName(villainName, "villains", "name");

        if (nameVillain == null) {
            addData("villains", "evilness_factor", villainName, "evil");
            System.out.printf("Villain %s was added to the database.%n", villainName);
        }

        preparedStatement = connection.prepareStatement(
                "INSERT INTO minions (name, age, town_id)\n" +
                        "VALUE (?,?, (SELECT id FROM towns WHERE name = ?));");
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, age);
        preparedStatement.setString(3, town);
        preparedStatement.executeUpdate();
        System.out.printf("Successfully added %s to be minion of %s.%n", name, villainName);


    }

    private static void exFive() throws SQLException, IOException {
        System.out.println("Enter country name");
        String countryName = reader.readLine();

        preparedStatement = connection.prepareStatement(
                "UPDATE towns" +
                        " SET name = UPPER(name)" +
                        " WHERE country = ?;");

        preparedStatement.setString(1, countryName);

        int countAffected = preparedStatement.executeUpdate();

        if (countAffected == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        System.out.printf("%d town names were affected.%n", countAffected);

        String result = (checkEntityExistByName(countryName, "towns", "country"));

        List<String> updatesTowns = new ArrayList<>();
        updatesTowns.add(result);

        while (resultSet.next()) {
            updatesTowns.add(resultSet.getString("name"));
        }

        System.out.println(updatesTowns.toString().replaceAll("[,]", ","));


    }

    private static void exSix() throws IOException, SQLException {
        System.out.println("Enter villain id");
        int villainId = Integer.parseInt(reader.readLine());

        preparedStatement = connection.prepareStatement(
                "SELECT villain_id, COUNT(minion_id) AS count FROM minions_villains\n" +
                        "WHERE villain_id = ?\n" +
                        "GROUP BY villain_id;");

        preparedStatement.setInt(1, villainId);
        resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            System.out.println("No such villain was found");
            return;
        }

        int deletedMinions = resultSet.getInt("count");
        String name = checkIfEntityExist(villainId, "villains", "id");

        preparedStatement = connection.prepareStatement("DELETE FROM minions_villains \n" +
                "WHERE villain_id = ?;");
        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();


        preparedStatement = connection.prepareStatement("DELETE FROM villains\n" +
                "WHERE id = ?;");
        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();

        System.out.printf("%s was deleted%n", name);
        System.out.printf("%d minions released%n", deletedMinions);


    }

    private static void exSeven() throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT name FROM minions;");
        resultSet = preparedStatement.executeQuery();
        List<String> names = new ArrayList<>();
        while (resultSet.next()) {
            names.add(resultSet.getString("name"));
        }

        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i));
            System.out.println(names.get(names.size() - 1));
            names.remove(names.size() - 1);
        }
    }

    private static void exEight() throws IOException, SQLException {
        System.out.println("Enter minion id");
        int id = Integer.parseInt(reader.readLine());
        preparedStatement = connection.prepareStatement(
                "UPDATE minions\n" +
                        "SET age = age + 1, name = LOWER(name)\n" +
                        "WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement("SELECT id, name, age FROM minions;");
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%d %s %d%n", resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("age"));
        }


    }

    private static void exNine() throws IOException, SQLException {
        System.out.println("Enter minion id");
        int minionId = Integer.parseInt(reader.readLine());

        CallableStatement callableStatement = connection.prepareCall("CALL usp_get_older(?);");
        callableStatement.setInt(1, minionId);
        callableStatement.executeUpdate();

        preparedStatement = connection.prepareStatement("SELECT name, age FROM minions WHERE id = ?");
        preparedStatement.setInt(1, minionId);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        System.out.printf("%s %d%n", name, age);


    }

    private static String checkIfEntityExist(int id, String tableName, String param) throws SQLException {
        String query = String.format("SELECT name FROM %s WHERE %s = ?;", tableName, param);
        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("name");
    }

    private static void addData(String tableName, String secondParam, String name, String param) throws SQLException {
        String query = String.format("INSERT INTO %s (name, %s) VALUE (?,?);", tableName, secondParam);
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, param);
        preparedStatement.executeUpdate();
    }

    private static String checkEntityExistByName(String name, String tableName, String firsParam) throws SQLException {
        String query = String.format("SELECT name FROM %s WHERE %s = ?;", tableName, firsParam);
        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, name);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("name");
    }

    private static Connection getConnection() throws IOException, SQLException {
        System.out.println("Enter user");
        String user = reader.readLine();

        System.out.println("Enter password");
        String password = reader.readLine();


        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);


        return DriverManager
                .getConnection(CONNECTION_STRING + DB_NAME, properties);

    }
}
