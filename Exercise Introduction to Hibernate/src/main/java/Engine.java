import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Enter exercise number:");
        try {
            int exNum = Integer.parseInt(bufferedReader.readLine());
            switch (exNum) {
                case 2 -> exTwo2ChangeCasing();
                case 3 -> exThreeContainsEmployee();
                case 4 -> exFourEmployeesWithSalaryOver50000();
                case 5 -> exFiveEmployeesFromDepartment();
                case 6 -> exSixAddingANewAddressAndUpdatingEmployee();
                case 7 -> exSevenAddressesWithEmployeeCount();
                case 8 -> exEightGetEmployeeWithProject();
                case 9 -> exNineFindLatest10Projects();
                case 10 -> exTenIncreaseSalaries();
                case 11 -> ExElevenFindEmployeesByFirstName();
                case 12 -> ExTwelveEmployeesMaximumSalaries();
                case 13 -> ExThirteenRemoveTowns();
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            entityManager.close();
        }


    }

    private void ExThirteenRemoveTowns() throws IOException {
        System.out.println("Enter town: ");
        String townName = bufferedReader.readLine();

        Town town = entityManager.createQuery("SELECT t FROM Town t " +
                        "WHERE t.name = :t_name", Town.class)
                .setParameter("t_name", townName)
                .getSingleResult();

        int affected = removeAddressesByTown(town.getId());

        entityManager.getTransaction().begin();
        entityManager.remove(town);
        entityManager.getTransaction().commit();

        System.out.printf("%d address in %s deleted",
                affected, townName);

    }

    private int removeAddressesByTown(Integer id) {
        List<Address> resultList = entityManager.createQuery("SELECT a FROM Address a " +
                        "WHERE a.town.id = :p_id", Address.class)
                .setParameter("p_id", id)
                .getResultList();
        entityManager.getTransaction().begin();
        resultList.forEach(entityManager::remove);
        entityManager.getTransaction().commit();
        return resultList.size();
    }

    private void ExTwelveEmployeesMaximumSalaries() {


        List<Object[]> list= entityManager
                .createNativeQuery("SELECT d.name,MAX(e.salary) AS `m_salary` " +
                        "FROM departments d " +
                        "join employees e on d.department_id = e.department_id group by d.name" +
                        " HAVING m_salary NOT BETWEEN  30000 AND 70000;")
                .getResultList();
        System.out.println();
        for (Object[] objects : list) {
            System.out.printf("%s %s%n", objects[0], objects[1]);
        }

//        BigDecimal from = new BigDecimal(30000L);
//        BigDecimal to = new BigDecimal(70000L);
//
//        List<Department> resultList = entityManager.createQuery(
//                        "SELECT d FROM Department d " +
//                                "WHERE d.manager.salary NOT BETWEEN :f_salry AND :s_salary", Department.class)
//
//                .setParameter("f_salry", from)
//                .setParameter("s_salary", to)
//                .getResultList();
//
//
//
//        resultList.forEach(d -> System.out.printf("%s %.2f%n", d.getName(), d.getManager().getSalary()));
    }

    private void ExElevenFindEmployeesByFirstName() throws IOException {
        System.out.println("Enter the pattern with which the employee's first name begins:");
        String pattern = bufferedReader.readLine() + "%";
        List<Employee> resultList = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.firstName LIKE :e_pattern", Employee.class)
                .setParameter("e_pattern", pattern)
                .getResultList();
        for (Employee employee : resultList) {
            System.out.printf("%s %s - %s - ($%.2f)%n", employee.getFirstName(),
                    employee.getLastName(),
                    employee.getJobTitle(),
                    employee.getSalary());
        }
    }

    private void exTenIncreaseSalaries() {
        List<Integer> departments = List.of(1, 2, 4, 11);
        entityManager.getTransaction().begin();
        entityManager.createQuery("UPDATE Employee e " +
                        "SET e.salary = e.salary  * 1.12 " +
                        "WHERE e.department.id IN (:d_name)")
                .setParameter("d_name", departments)
                .executeUpdate();

        entityManager.getTransaction().commit();

        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.id IN (:d_name)", Employee.class)
                .setParameter("d_name", departments)
                .getResultList();

        for (Employee employee : employees) {
            System.out.printf("%s %s ($%.2f)%n", employee.getFirstName(), employee.getLastName(),
                    employee.getSalary());
        }


    }

    private void exNineFindLatest10Projects() {
        List<Project> projects = entityManager.createQuery(
                        "SELECT p FROM Project p " +
                                "ORDER BY p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss'.0'");

        projects.stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> {
                    System.out.printf("Project name: %s%n" +
                                    "\tProject Description: %s%n" +
                                    "\tProject Start Date:%s%n" +
                                    "\tProject End Date: %s%n"
                            , p.getName(),
                            p.getDescription(),
                            dateTimeFormatter.format(p.getStartDate()),
                            p.getEndDate() == null
                    ? "null" : dateTimeFormatter.format(p.getEndDate()));

                });

    }

    private void exEightGetEmployeeWithProject() throws IOException {
        System.out.println("Enter employee id:");
        int empId = Integer.parseInt(bufferedReader.readLine());

        Employee singleResult = entityManager.createQuery(
                        "SELECT e FROM Employee e " +
                                "WHERE e.id = :e_id " +
                                "ORDER BY e.projects.size", Employee.class)
                .setParameter("e_id", empId)
                .getSingleResult();

        System.out.printf("%s %s - %s%n",
                singleResult.getFirstName(),
                singleResult.getLastName(),
                singleResult.getJobTitle());

        singleResult.getProjects().stream().sorted((e1, e2) -> {
            int result = e1.getName().compareTo(e2.getName());
            if (result == 0) {
                result = e2.getName().compareTo(e1.getName());
            }
            return result;

        }).map(Project::getName).forEach(System.out::println);

    }

    private void exSevenAddressesWithEmployeeCount() {
        List<Address> resultList = entityManager.createQuery(
                        "SELECT a FROM Address a " +
                                "ORDER BY a.employees.size DESC ", Address.class)
                .setMaxResults(10)
                .getResultList();

        for (Address address : resultList) {
            System.out.printf("%s, %s - %d%n",
                    address.getText(),
                    address.getTown() == null
                            ? "Unknown" : address.getTown().getName(),
                    address.getEmployees().size());
        }


    }

    private void exSixAddingANewAddressAndUpdatingEmployee() throws IOException {

        System.out.println("Enter the last name of the employee:");
        String lastName = bufferedReader.readLine();

        Employee employee = entityManager.createQuery(
                        "SELECT e FROM Employee e " +
                                "WHERE e.lastName = :last_name", Employee.class)
                .setParameter("last_name", lastName)
                .getSingleResult();

        Address address = new Address();

        address.setText("Vitoska 15");
        entityManager.getTransaction().begin();
        entityManager.persist(address);
        employee.setAddress(address);
        entityManager.getTransaction().commit();

    }

    private void exFiveEmployeesFromDepartment() {
        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.name = :d_name " +
                        "ORDER BY e.salary, e.id", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultList();

        for (Employee employee : employees) {
            System.out.printf("%s %s from %s - $%.2f%n",
                    employee.getFirstName(), employee.getLastName(),
                    employee.getDepartment().getName(), employee.getSalary());
        }
    }

    private void exFourEmployeesWithSalaryOver50000() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.salary > :min_salary", Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);


    }

    private void exThreeContainsEmployee() throws IOException {
        System.out.println("Enter employee full name:");
        String[] input = bufferedReader.readLine().split("\\s+");
        String firstName = input[0];
        String lastName = input[1];

        Long singleResult = entityManager.createQuery(
                        "SELECT COUNT (e) FROM Employee e " +
                                "WHERE e.firstName = :first_name " +
                                "AND e.lastName = :last_name", Long.class)
                .setParameter("first_name", firstName)
                .setParameter("last_name", lastName)
                .getSingleResult();


        System.out.println(singleResult == 0 ? "No" : "Yes");

    }

    private void exTwo2ChangeCasing() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE Town t " +
                "SET t.name = UPPER(t.name) " +
                "WHERE LENGTH(t.name) <= 5");

        query.executeUpdate();
        entityManager.getTransaction().commit();
    }
}
