package DaoImpl;

import Dao.EmployeeDao;
import Model.Employee;
import Model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl extends JobDaoImpl implements EmployeeDao {
    Connection connection;
    @Override
    public void createEmployee() {
        String sql="CREATE TABLE IF NOT EXISTS Employee" +
                "id SERIAL PRIMARY KEY," +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "age INT," +
                "email VARCHAR(50)" +
                "job_id REFERENCES JOB(id) ";


        Statement statement;
        try {
            statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully created!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employee(firsName,lastName,email,age,jobId)" +
                " VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setInt(4, employee.getAge());
            preparedStatement.setInt(5, employee.getJobId().getExperience());
            System.out.println("Successfully added!");
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        {

        }
    }

    @Override
    public void dropTable() {
        String sql="DROP TABLE IF NOT EXISTS FROM Employee";

Statement statement;
        try {
            statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully drop!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cleanTable() {
        String sql="DELETE  * FROM Employee";
Statement statement;
        try {
            statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully cleaned!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {

        String sql = "UPDATE Employee SET firstName = ?, lastName = ?, age = ?, email = ?, jobId = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setInt(3, employee.getAge());
            statement.setString(4, employee.getEmail());
            statement.setLong(5, employee.getId());
            statement.setLong(6, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("No employee found with the specified ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee", e);
        }
        }


    @Override
    public List<Employee> getAllEmployees() {
        List<Employee>employees =new ArrayList<>();
        Statement statement = null;

        String sql="select * from Employee";
        try {
            statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));

            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
        }


    @Override
    public Employee findByEmail(String email) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = new Employee();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM \"Employee\" WHERE email=?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("firstName"));
                employee.setLastName(resultSet.getString("lastName"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return employee;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee, Job> map = new LinkedHashMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql="SELECT e.id AS e_id, e.firstName, e.lastName, e.age, e.email,\n" +
                "                   j.id AS j_id, j.position, j.profession, j.experience, j.description\n" +
                "            FROM \"Employee\" e\n" +
                "            INNER JOIN \"Job\" j ON e.job_id = j.id\n" +
                "            WHERE e.id = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employeeId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e_id"));
                employee.setFirstName(resultSet.getString("firstName"));
                employee.setLastName(resultSet.getString("lastName"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));

                Job job = new Job();
                job.setId(resultSet.getLong("j_id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setExperience(resultSet.getInt("experience"));
                job.setDescription(resultSet.getString("description"));

                map.put(employee, job);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return map;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {

        List<Employee> employees = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql=" SELECT e.id, e.firstName, e.lastName, e.age, e.email, e.job_id\n" +
                "            FROM \"Employee\" e\n" +
                "            INNER JOIN \"Job\" j ON e.job_id = j.id\n" +
                "            WHERE j.position = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, position);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return employees;
    }
}
