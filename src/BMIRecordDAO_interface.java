import java.sql.SQLException;
import java.util.List;

public interface BMIRecordDAO_interface {
    void insert(BMIRecord record) throws SQLException;
    void update(BMIRecord record) throws SQLException;
    void delete(int id) throws SQLException;
    BMIRecord getById(int id) throws SQLException;
    List<BMIRecord> getAll() throws SQLException;
    List<BMIRecord> queryByCondition(String condition) throws SQLException;
} 