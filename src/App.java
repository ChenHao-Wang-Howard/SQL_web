import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {
        // 連結資料庫
        String jdbcURL = "jdbc:mysql://localhost:3306/BMIDatabase";
        String username = "root";
        String password = "35172846";
        String sql = "INSERT INTO BMITable (bmi_range, status, count, percentage) VALUES (?, ?, ?, ?)";
        String sqlSelect = "SELECT * FROM BMITable";
        String csvFilePath = "BMITableData.csv";

        // Register MySQL JDBC driver
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Register MySQL JDBC driver successful");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            return;
        }

        // 測試資料庫連接
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            logger.info("Database connection successful");
            URL url = new URL(
                    "https://data.taipei/api/dataset/0d1cee1e-4963-41b5-bf9d-93dd5e413cea/resource/f0c12e09-f7e7-4ffa-a028-6933dd5cc12e/download");
            try (InputStream input = url.openStream();
                    InputStreamReader isr = new InputStreamReader(input, "MS950");
                    BufferedReader br = new BufferedReader(isr)) {

                String str = "";
                while ((str = br.readLine()) != null) {
                    // 跳過第一行
                    if (str.startsWith("BMI值體位")) {
                        continue;
                    }

                    String[] split = str.split(",");
                    if (split.length == 4) {
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, split[0].trim());
                            statement.setString(2, split[1].trim());
                            statement.setInt(3, Integer.parseInt(split[2].trim()));
                            statement.setString(4, split[3].trim());
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            logger.log(Level.SEVERE, "Database insertion error: " + str, e);
                        }
                    } else {
                        logger.warning("Invalid data line: " + str);
                    }
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error reading URL", e);
            }

            // 將資料庫檔案寫入CSV
            try (PreparedStatement statement = connection.prepareStatement(sqlSelect);
                    ResultSet resultSet = statement.executeQuery();
                    BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath))) {

                // CSV第一行為類別欄位
                csvWriter.write("bmi_range,status,count,percentage");
                csvWriter.newLine();

                // 接著寫入data
                while (resultSet.next()) {
                    String bmiRange = resultSet.getString("bmi_range");
                    String status = resultSet.getString("status");
                    int count = resultSet.getInt("count");
                    String percentage = resultSet.getString("percentage");

                    String row = String.format("%s,%s,%d,%s", bmiRange, status, count, percentage);
                    csvWriter.write(row);
                    csvWriter.newLine();
                }

                logger.info("Data exported to CSV file successfully");
            } catch (SQLException | IOException e) {
                logger.log(Level.SEVERE, "Error exporting data to CSV", e);
            }

        } catch (SQLException | MalformedURLException e) {
            logger.log(Level.SEVERE, "Failed to connect to the database", e);
            return;
        }

    }
}
