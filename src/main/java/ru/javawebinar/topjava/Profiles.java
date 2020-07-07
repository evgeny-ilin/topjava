package ru.javawebinar.topjava;

public class Profiles {
    public static final String
            JDBC = "jdbc",
            JPA = "jpa",
            DATAJPA = "datajpa";

    public static final String REPOSITORY_IMPLEMENTATION = JDBC;

    public static final String
            POSTGRES_DB = "postgres",
            HSQL_DB = "hsqldb";

    //  Get DB profile depending of DB driver in classpath
    public static String[] getProfile() {
        try {
            Class.forName("org.postgresql.Driver");
            return new String[]{POSTGRES_DB, REPOSITORY_IMPLEMENTATION};
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                return new String[]{HSQL_DB, REPOSITORY_IMPLEMENTATION};
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not find DB driver");
            }
        }
    }
}