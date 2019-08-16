import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserServiceMapImpl implements IUsersService {
    private Map<String,User> userMap;

    public UserServiceMapImpl(){
        userMap = new HashMap<String,User>();
    }

    @Override
    public Collection<User> getUsers() {
        return userMap.values();
    }

    @Override
    public User getUser(String username) {
        return userMap.get(username);
    }

    @Override
    public int addUser(User user) throws ApiException {
        userMap.put(user.getUsername(),user);
        return userMap.size();
    }

    @Override
    public User updateUser(String username, User user) throws ApiException {
        userMap.remove(username);
        userMap.put(user.getUsername(),user);
        return userMap.get(username);
    }

    @Override
    public User deleteUser(String username) throws ApiException {
        userMap.remove(username);
        return null;
    }

    @Override
    public boolean userExsits(String username) {
        User element = userMap.get(username);
        Boolean returnBool;
        if (element == null) {
            returnBool = false;
        } else {
            returnBool = true;
        }
        return returnBool;
    }

}
