package ru.prudnikova.dataBase;

import org.junit.jupiter.api.Assertions;
import ru.prudnikova.domain.CallbackPhonesBD;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CallbackPhonesManager {
    private DataSource ds = DataSourceProvider.INSTANCE.getDataSource();

    public String selectLastEntryPhoneFromCallbackPhonesTables(){
        String result=null;
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT phone FROM callback_phones WHERE id=(SELECT max(id) FROM callback_phones);"
             )) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getString("phone");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
    }
        return result;
    }

    public CallbackPhonesBD selectLastEntryFromCallbackPhonesTables(){
        CallbackPhonesBD result=new CallbackPhonesBD();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT phone, user_id, link FROM callback_phones WHERE id=(SELECT max(id) FROM callback_phones);"
             )) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result = CallbackPhonesBD.builder()
                        .phone(resultSet.getString("phone"))
                        .userId(resultSet.getString("user_id"))
                        .link(resultSet.getString("link"))
                        .build();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}

