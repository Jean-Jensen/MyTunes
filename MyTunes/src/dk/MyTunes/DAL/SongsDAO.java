package dk.MyTunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SongsDAO {
    ConnectionManager con = new ConnectionManager();

    public void test(){
        try {
            Connection connect = con.getConnection();

            String command = "SELECT * FROM Songs";
            Statement state = connect.createStatement();
            ResultSet rs = state.executeQuery(command);

            while(rs.next()){
                int id = rs.getInt("id");
                System.out.println(id);
                System.out.println("Connection Established");
            }

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}