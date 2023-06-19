package Data_Base;

public class Querys {
    public static final String GET_USER = "SELECT Users.`id`, `first_name`, `last_name`, `second_name`, `number_phone`, `address`, `id_dc`, `login`, `password`, `name` FROM Users\n" +
            "JOIN Role_User ON Users.id = Role_User.id_user\n" +
            "JOIN Roles on Role_User.id_role = Roles.id\n" +
            "WHERE login = ?;";
    public static final String UPDATE_USER = "UPDATE `Users` \n" +
            "SET first_name = ?\n" +
            "WHERE login = ?;";

    public static String QUERY_LATER = "";
}
