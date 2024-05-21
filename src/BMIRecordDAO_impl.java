import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class BMIRecordDAO_impl implements BMIRecordDAO_interface {
    private static final String URL = "jdbc:mysql://localhost:3306/BMIDatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "35172846";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public void insert(BMIRecord record) throws SQLException {
        String sql = "INSERT INTO BMITable (bmi_range, status, count, percentage) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, record.getBmiRange());
            statement.setString(2, record.getStatus());
            statement.setInt(3, record.getCount());
            statement.setString(4, record.getPercentage());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(BMIRecord record) throws SQLException {
        String sql = "UPDATE BMITable SET bmi_range = ?, status = ?, count = ?, percentage = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, record.getBmiRange());
            statement.setString(2, record.getStatus());
            statement.setInt(3, record.getCount());
            statement.setString(4, record.getPercentage());
            statement.setInt(5, record.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM BMITable WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public BMIRecord getById(int id) throws SQLException {
        String sql = "SELECT * FROM BMITable WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new BMIRecord(
                        resultSet.getInt("id"),
                        resultSet.getString("bmi_range"),
                        resultSet.getString("status"),
                        resultSet.getInt("count"),
                        resultSet.getString("percentage"));
            }
        }
        return null;
    }

    @Override
    public List<BMIRecord> getAll() throws SQLException {
        List<BMIRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM BMITable";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(new BMIRecord(
                        resultSet.getInt("id"),
                        resultSet.getString("bmi_range"),
                        resultSet.getString("status"),
                        resultSet.getInt("count"),
                        resultSet.getString("percentage")));
            }
        }
        return list;
    }

    @Override
    public List<BMIRecord> queryByCondition(String condition) throws SQLException {
        List<BMIRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM BMITable WHERE " + condition;
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(new BMIRecord(
                        resultSet.getInt("id"),
                        resultSet.getString("bmi_range"),
                        resultSet.getString("status"),
                        resultSet.getInt("count"),
                        resultSet.getString("percentage")));
            }
        }
        return list;
    }
    
}
