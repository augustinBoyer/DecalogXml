import org.apache.poi.hpsf.GUID;

import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.UUID;

public class Context {

    public Context() {};

    Connection conn;


        /**
         * Connect to the test.db database
         *
         * @return the Connection object
         */
        private Connection connect(String name) {
            // SQLite connection string
            String url = "jdbc:sqlite:C:\\Users\\Augustin\\Desktop/test.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);

                    //createNewTable(name);


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }

    private Connection connect(String name, String url) {
        // SQLite connection string

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);

            createNewTable(name, "capacity");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewTable(String name, String text) {
        String url = "jdbc:sqlite:C:\\Users\\Augustin\\Desktop/test.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + name + "	(id PRIMARY KEY NOT NULL, " + text + " text, Titre text);";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(Hashtable<String, String> data, String capacity) {
        String sql = "INSERT INTO " + capacity + "(id,capacity) VALUES(?,?)";

        try (Connection conn = this.connect(capacity);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Enumeration enu = data.keys();

            int i = 0;
            while(enu.hasMoreElements()){
                String k = enu.nextElement().toString();
                pstmt.setString(1, k);
                pstmt.setString(2, data.get(k));
                pstmt.executeUpdate();
                i++;
            }

            System.out.println(i);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String data, String capacity, String sql) {



        try (Connection conn = this.connect(capacity, "jdbc:sqlite:C:\\Users\\Augustin\\Desktop/test.db");

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            createNewTable("Ids", "Data");


                pstmt.setString(1, UUID.randomUUID().toString());
                pstmt.setString(2, data);
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void insertTag(ArrayList<Tag> data, String capacity, String sql) {

        try (Connection conn = this.connect(capacity, "jdbc:sqlite:C:\\Users\\Augustin\\Desktop/test.db");

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < data.size(); i++){


                pstmt.setString(1, data.get(i).Parent);
                pstmt.setString(2, data.get(i).Name);
                pstmt.setString(3, data.get(i).Schema);
                pstmt.setString(4, data.get(i).Id.toString());
                pstmt.executeUpdate();
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(ArrayList<String> data, ArrayList<String> titles, String capacity) {
        String sql = "INSERT INTO " + capacity + "(id, Data, Titre) VALUES(?, ?, ?)";
        createNewTable(capacity, "Data");
        try (Connection conn = this.connect(capacity);


             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < data.size(); i++){
                pstmt.setString(1, UUID.randomUUID().toString());
                pstmt.setString(2, data.get(i));
                pstmt.setString(3, titles.get(i));
                pstmt.executeUpdate();

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*public String GetById(String nameOftable, String id){
        try (Connection conn = this.connect(nameOftable);

             String sql = "INSERT INTO " + capacity + "(id,capacity) VALUES(?,?)";

             PreparedStatement pstmt = conn.prepareStatement(sql)) {



            System.out.println(i);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:\\Users\\Augustin\\Desktop/test.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public ArrayList<Tag> getTag(String scheme) {
        String sql = "SELECT * FROM tag where scheme = ?";


        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, scheme);
            //
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Tag> tags = new ArrayList<>();

            // loop through the result set
            while (rs.next()) {

                Tag tag = new Tag();
                tag.Id = rs.getString("Id");
                tag.Parent = rs.getString("Parent");
                tag.Name =        rs.getString("Scheme");
                tag.Schema = scheme;

                tags.add(tag);
            }

            return tags;
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        return null;
    }
}
