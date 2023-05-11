package ru.prudnikova.dataBase;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public enum DataSourceProvider {
    INSTANCE;
    private DataSource dataSource;
    public DataSource getDataSource() {
        if (dataSource == null) {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setUrl("jdbc:mysql://agency.novo-estate.ru:3306/agency_novo-estate_ru?useSSL=false");
            mysqlDataSource.setUser("4063356894");
            mysqlDataSource.setPassword("9AZ3M$YAXiEA6");
            dataSource = mysqlDataSource;
        }
        return dataSource;
    }
}
