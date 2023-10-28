package org.example;

import org.example.models.MaxProjectCountClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {
    private Connection connection;

    public Executor(Connection connection){
        this.connection = connection;
    }
    public int executeQuery(String query){
        try(Statement statement = this.connection.createStatement()){
            return statement.executeUpdate(query);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Не можливо виконати послідовність");
        }
    }
    public  void closeConnection(){
        try{
            this.connection.close();
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
    }
}
