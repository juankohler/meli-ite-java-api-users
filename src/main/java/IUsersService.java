import java.util.Collection;

public interface IUsersService {
    public Collection<User> getUsers();
    public User getUser(String username);
    public int addUser(User User) throws ApiException;
    public User updateUser(String username, User User) throws ApiException;
    public User deleteUser(String username) throws ApiException;
    public boolean userExsits(String username);
}
