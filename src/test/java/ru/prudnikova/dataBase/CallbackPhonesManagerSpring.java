package ru.prudnikova.dataBase;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.prudnikova.domain.CallbackPhonesBD;

import java.util.List;


public class CallbackPhonesManagerSpring{
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(
            DataSourceProvider.INSTANCE.getDataSource()
    );

    public String selectLastEntryPhoneFromCallbackPhonesTables() {
        String sql = "SELECT phone FROM callback_phones WHERE id=(SELECT max(id) FROM callback_phones);";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public List<CallbackPhonesBD> selectLastEntryFromCallbackPhonesTables(){
        return jdbcTemplate.query("SELECT phone, user_id, link FROM callback_phones WHERE id=(SELECT max(id) FROM callback_phones);",
                new BeanPropertyRowMapper<>(CallbackPhonesBD.class, false));
    }

}
