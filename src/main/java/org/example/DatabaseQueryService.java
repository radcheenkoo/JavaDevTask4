package org.example;

import org.example.models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseQueryService {
    public static void main(String[] args) {
        DatabaseQueryService databaseQueryService = new DatabaseQueryService();
        databaseQueryService.findLongestProject();
        databaseQueryService.findMaxProject();
        databaseQueryService.findMaxSalaryWorker();
        databaseQueryService.findYoungestEldestWorkers();
    }
    public List<LongestProject> findLongestProject() {
        return executeQuery("sql/find_longest_project.sql",
                "id", "months_difference", LongestProject::new);
    }

    public List<MaxProjectCountClient> findMaxProject() {
        return executeQuery("sql/find_max_projects_client.sql",
                "client_name", "num_projects", MaxProjectCountClient::new);
    }

    public List<MaxSalaryWorker> findMaxSalaryWorker() {
        return executeQuery("sql/find_max_salary_worker.sql",
                "name", "salary", MaxSalaryWorker::new);
    }

    public List<YoungestEldestWorkers> findYoungestEldestWorkers() {
        return executeQuery("sql/find_youngest_eldest_workers.sql",
                "name", "birthday", YoungestEldestWorkers::new);
    }


    private <T> List<T> executeQuery(String sqlFilePath, String column1, String column2, ResultSetMapper<T> mapper) {
        List<T> result = new ArrayList<>();

        try {
            Connection connection = Database.getInstance().getConnection();
            String query = readFileContent(sqlFilePath);

            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    T item = mapper.map(resultSet.getString(column1), resultSet.getString(column2));
                    result.add(item);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private interface ResultSetMapper<T> {
        T map(String column1, String column2);
    }
}
