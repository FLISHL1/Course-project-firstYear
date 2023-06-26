package data_Base.query;

public class Query {
    public static final String GET_USER = "SELECT Users.`id`, `first_name`, `last_name`, `second_name`, `number_phone`, `address`, `id_dc`, `login`, `password`, `name` FROM Users\n" +
            "JOIN Role_User ON Users.id = Role_User.id_user\n" +
            "JOIN Roles on Role_User.id_role = Roles.id\n" +
            "%s;";

    public static final String GET_TABLE = "SELECT * FROM %s \n %s;";
    public static final String UPDATE = "UPDATE %s \n" +
            "SET %s\n" +
            "%s;";
    public static final String INSERT = "INSERT INTO %s \n" +
            "SET %s;";

    public static final String DEL_ROW = "DELETE FROM %s %s;";
    public static final String INSERT_ROLE = "INSERT INTO Role_User\n" +
            "(id_user, id_role)\n" +
            "SELECT Users.id, %s FROM Users\n" +
            "%s;";

}
