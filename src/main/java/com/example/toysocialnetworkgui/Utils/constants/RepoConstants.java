package com.example.toysocialnetworkgui.Utils.constants;

public class RepoConstants {

    //Sql instructions for 'users' table
    public static final String FIND_USER_BY_ID_DB = "select id,first_name,last_name,password from users where id = ?";
    public static final String DELETE_USER_DB = "delete from users where id = ?";
    public static final String UPDATE_USER_DB = "update users set first_name = ?,last_name = ? where id = ?";
    public static final String SAVE_USER_DB = "insert into users (first_name, last_name,password ) values (?, ?, ?)";
    public static final String SELECT_ALL_USERS_DB = "SELECT * from users";
    public static final String FIND_USER_BY_FIRST_AND_LAST_NAME = "select id,first_name,last_name from users where first_name = ? and last_name = ?";
    public static final String FIND_USER_BY_USERNAME_DB = "SELECT id,first_name,last_name,password from users where first_name = ?";
    public static final String FIND_USER_BY_USERLASTNAME_DB = "SELECT id,first_name,last_name,password from users where last_name = ?";

   //Return codes for functions
    public static final int SUCCESFUL_OPERATION_RETURN_CODE = 0;
    public static final int UNSUCCESFUL_OPERATION_RETURN_CODE = 1;

    //Sql instructions for 'friendships' table
    public static final String FIND_FRIENDSHIP_BY_BOTH_ID_DB = "select id_1,id_2,data_crearii from friendships where id_1 = ? and id_2 = ?";
    public static final String DELETE_FRIENDSHIP_DB = "delete from friendships where id_1 = ? and id_2= ?";
    public static final String UPDATE_FRIENDSHIP_DB = "update friendships set data_crearii = ? where id_1 = ? and id_2 = ?";
    public static final String SAVE_FRIENDSHIP_DB = "insert into friendships (id_1, id_2,data_crearii,friendship_status,sender ) values (?, ?, ?, ?, ?)";


    //Sql instructions for 'messages' table
    public static final String FIND_MESSAGE_BY_ID_DB = "select id_mesaj,id_from,id_to,mesaj,data_trimitere,id_reply,delete_status "+
                                                    "from messages where id_mesaj = ?";
    public static final String SELECT_ALL_MESSAGES_DB = "select * from messages";
    public static final String SAVE_MESSAGE_DB = "insert into messages(id_from,id_to,mesaj,data_trimitere,id_reply,delete_status) values(?,?,?,?,?,?)";
    public static final String DELETE_MESSAGE_DB = "update messages "+
                                                    "set delete_status = ? "+
                                                    "where id_mesaj = ?";
    public static final String UPDATE_MESSAGE_DB =  "update messages "+
                                                    "set delete_status = ? "+
                                                    "where id_mesaj = ?";
}
