package org.example;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        List<User> userList = collectUsers();

        FileOutputStream outputStream = new FileOutputStream("D:\\File\\UserZip.zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        ZipEntry zipEntry = new ZipEntry("userList1.txt");
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(userList.stream().map(user -> user.toString()).limit(2).collect(Collectors.joining("\n")).getBytes());
        zipOutputStream.closeEntry();

        ZipEntry zipEntry1 = new ZipEntry("userList2.txt");
        zipOutputStream.putNextEntry(zipEntry1);
        zipOutputStream.write(userList.stream().sorted(Comparator.comparingInt(User::getId).reversed()).map(user -> user.toString()).limit(2).collect(Collectors.joining("\n")).getBytes());
        zipOutputStream.closeEntry();

        zipOutputStream.close();
        outputStream.close();
    }

    public static List<User> collectUsers(){
        User user = null;
        List<User> userList = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","root","Abi@123#");
            Statement statement = connection.createStatement();;
            ResultSet rs = statement.executeQuery("select * from user order by id");
            userList = new ArrayList<User>();
            while(rs.next()){
                user = new User(rs.getInt("id"),rs.getString("name"),rs.getString("emailId"));
                userList.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return userList;
    }
}