package DaoImpl;

import Config.JdbsConfig;
import Dao.EmployeeDao;
import Dao.JobDao;
import Model.Employee;
import Model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobDaoImpl implements EmployeeDao, JobDao {
    private final Connection connection= JdbsConfig.getConnection();
    EmployeeDaoImpl employeeDao;
    @Override
    public void createEmployee() {
        employeeDao.createEmployee();
    }

    @Override
    public void addEmployee(Employee employee) {
employeeDao.addEmployee(employee);
    }

    @Override
    public void dropTable() {
employeeDao.dropTable();
    }

    @Override
    public void cleanTable() {
employeeDao.cleanTable();
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
employeeDao.updateEmployee(id,employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> emm =employeeDao.getAllEmployees();
        return emm;

    }

    @Override
    public Employee findByEmail(String email) {
        Employee find=employeeDao.findByEmail(email);
        return find;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee,Job> emm=employeeDao.getEmployeeById(employeeId);
        return emm;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee> emm =employeeDao.getEmployeeByPosition(position);
        return emm;

    }

    @Override
    public void createJobTable() {
            try (Connection connection = JdbsConfig.getConnection();
                 Statement statement = connection.createStatement()) {
                String query = "CREATE TABLE IF NOT EXISTS job (" +
                        "id SERIAL PRIMARY KEY," +
                        "position VARCHAR(255) NOT NULL," +
                        "department VARCHAR(255) NOT NULL," +
                        "description TEXT," +
                        "experience INT NOT NULL" +
                        ")";
                statement.executeUpdate(query);
                System.out.println("Job table created successfully.");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create Job table", e);
            }
        }


    @Override
    public void addJob(Job job) {
        String sql = "INSERT INTO Job (position, profession, description,experience) VALUES (?, ?, ? ,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, job.getPosition());
            statement.setString(2, job.getProfession());
            statement.setString(3, job.getDescription());
            statement.setInt(3, job.getExperience());

            statement.executeUpdate();
            System.out.println("Job added successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add Job", e);
        }
    }

    @Override
    public Job getJobById(Long jobId) {
        String sql = "SELECT * FROM Job WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, jobId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String position = resultSet.getString("position");
                    String profession = resultSet.getString("profession");
                    String description = resultSet.getString("description");
                    int experience = resultSet.getInt("experience");

                    return new Job(jobId, position, profession, description, experience);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Job by ID", e);
        }

        return null;
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        String sql = "SELECT * FROM Job ORDER BY experience " + ascOrDesc;

        List<Job> jobs = new ArrayList();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Long jobId = resultSet.getLong("id");
                String position = resultSet.getString("position");
                String profession = resultSet.getString("profession");
                String description = resultSet.getString("description");
                int experience = resultSet.getInt("experience");

                Job job = new Job(jobId, position, profession, description, experience);
                jobs.add(job);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to sort Jobs by experience", e);
        }

        return jobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        String sql = "SELECT j.* FROM Job j INNER JOIN Employee e ON j.id = e.jobId WHERE e.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, employeeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long jobId = resultSet.getLong("id");
                    String position = resultSet.getString("position");
                    String profession = resultSet.getString("profession");
                    String description = resultSet.getString("description");
                    int experience = resultSet.getInt("experience");

                    return new Job(jobId, position, profession, description, experience);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get Job by Employee ID", e);
        }

        return null;
    }

    @Override
    public void deleteDescriptionColumn() {
        String sql = "ALTER TABLE Job DROP COLUMN description";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Description column deleted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete Description column", e);
        }
    }
}
