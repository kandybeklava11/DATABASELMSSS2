package Main;

import Config.JdbsConfig;
import Dao.EmployeeDao;
import DaoImpl.EmployeeDaoImpl;
import DaoImpl.JobDaoImpl;
import Model.Employee;
import Model.Job;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        JdbsConfig.getConnection();
        JobDaoImpl jobDao = new JobDaoImpl(); // Создание экземпляра JobDaoImpl, а не EmployeeDaoImpl
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine(); // Считывание ввода пользователя

            switch (input) {
                case "1" -> jobDao.createJobTable();
                case "2" -> {
                    Job job = new Job("junior Developer", "It", "develop bekend java", 1);
                    jobDao.addJob(job);
                }
                case "3" -> System.out.println(jobDao.getJobById(1L));
                case "4" -> System.out.println(jobDao.sortByExperience("asc"));
                case "5" -> System.out.println(jobDao.sortByExperience("desc"));
                case "6" -> System.out.println(jobDao.getJobByEmployeeId(1L));
                case "7" -> jobDao.deleteDescriptionColumn();
                case "8", "save" -> jobDao.createEmployee();
                case "9" -> {
                    Job job = jobDao.getJobById(1L);
                    Employee employee = new Employee("erkin", "toigonbaev", 25, "erin@gmail.com", job);
                    jobDao.addEmployee(employee);
                }
                case "10" -> jobDao.dropTable();
                case "11" -> {
                    Job job = jobDao.getJobById(1L);
                    Employee employee = new Employee("Erkin", "Toigonbaev", 19, "Er@gmail.com", job);
                    jobDao.updateEmployee(1L, employee);
                }
                case "12" -> jobDao.cleanTable();
                case "13" -> System.out.println(jobDao.getAllEmployees());
                case "14" -> System.out.println(jobDao.findByEmail("Er@gmail.com"));
                case "15" -> System.out.println(jobDao.getEmployeeById(1L));
                case "16" -> System.out.println(jobDao.getEmployeeByPosition("junior Developer"));
                default -> System.out.println("Invalid input");
            }
        }
    }
}
