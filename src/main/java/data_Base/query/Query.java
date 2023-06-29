package data_Base.query;

public class Query {
//    SELECT
    public static final String GET_USERS = "SELECT Users.`id`, `first_name`, `last_name`, `second_name`, `number_phone`, `address`, `id_dc`, `login`, `password`, `name` FROM Users\n" +
            "JOIN Role_User ON Users.id = Role_User.id_user\n" +
            "JOIN Roles on Role_User.id_role = Roles.id;";
    public static final String GET_USER = "SELECT Users.`id`, `first_name`, `last_name`, `second_name`, `number_phone`, `address`, `id_dc`, `login`, `password`, `name` FROM Users\n" +
            "JOIN Role_User ON Users.id = Role_User.id_user\n" +
            "JOIN Roles on Role_User.id_role = Roles.id\n" +
            "WHERE `login`=?;";

    public static final String GET_TABLE = "SELECT * FROM %s %s;";
//    UPDATE
    public static final String UPDATE_ROLE = "UPDATE `Role_User` \n" +
            "SET %s\n" +
            "WHERE `id_user`=?;";
    public static final String UPDATE_USER = "UPDATE `Users` \n" +
            "SET %s\n" +
            "WHERE `login`=?;";
    public static final String UPDATE_PACK = "UPDATE `Packs` \n" +
            "SET %s\n" +
            "WHERE `id`=?;";
//    INSERT
    public static final String INSERT = "INSERT INTO %s \n" +
            "SET %s;";
    public static final String INSERT_ROLE = "INSERT INTO Role_User\n" +
            "(id_user, id_role)\n" +
            "SELECT Users.id, ? FROM Users\n" +
            "WHERE `login`=?;";
// DEL
    public static final String DEL_USER = "DELETE FROM `Users` WHERE `login`=?;";
    public static final String DEL_PACK = "DELETE FROM `Packs` WHERE `id`=?;";


}
