import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static spark.Spark.*;
public class SparkUsers {
    public static void main(String[] args) {
        port(8095);
        final IUsersService service = new UserServiceMapImpl();
        //URL url =   getClass().getResource("ListStopWords.txt");
        Gson gson = new Gson();
        JsonReader readerUsers = null;
        try {
            readerUsers = new JsonReader(new FileReader((new File("src/main/java/users.json").getAbsoluteFile()).toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        User[] usersList = gson.fromJson(readerUsers, User[].class);
        //System.out.println(new Gson().toJsonTree(usersList));
        for (int i = 0; i < usersList.length; i++) {
            try {
                service.addUser(usersList[i]);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
        get("/users/sites/:username/:token", (req,res) -> {
            //res.type("application/json");
            //JSONObject getJSon = new JSONObject(req.body().toString());
            String usernameReq = req.params("username");//getJSon.getString("username");
            String tokenReq = req.params("token");//getJSon.getString("token");
            //System.out.println(usernameReq);
            if (service.getUser(usernameReq).getToken().equals(tokenReq)) {
                try {
                    URL url = new URL("http://localhost:8083/sites");
                    try {
                        URLConnection urlConnection = url.openConnection();
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                        if (urlConnection instanceof HttpURLConnection) {
                            HttpURLConnection connection = (HttpURLConnection) urlConnection;
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            Gson gsonSites = new Gson();
                            Site[] sites = gsonSites.fromJson(in, Site[].class);
                            return new Gson().toJsonTree(sites);
                        } else {
                            //System.out.println("URL INVALIDA.");
                            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "URL INVALIDA"));
                        }
                    } catch (IOException exception) {
                        System.out.println(exception.getMessage());
                        return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, exception.getMessage()));
                    }
                } catch (MalformedURLException exception) {
                    System.out.println(exception.getMessage());
                    return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, exception.getMessage()));
                }
            } else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "PETICION INVALIDA"));
            }
        });
        post("/users", (req,res) -> {
            res.type("application/json");
            User userPassword = new Gson().fromJson(req.body(), User.class);
            String username = userPassword.getUsername();
            String password = userPassword.getPassword();
            if (service.userExsits(username)) {
                if (service.getUser(username).getPassword().equals(password)){
                    double random = Math.random();
                    random = random * 1000;
                    int token = (int) random;
                    userPassword.setToken(String.valueOf(token));
                    service.updateUser(username,userPassword);
                    return new Gson().toJsonTree(service.getUser(username));
                }else{
                    return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,"PASSWORD INCORRECTA"));
                }
            }else{
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,"USUARIO NO EXISTE"));
            }
        });
        get("/users/sites/:id/categories/:username/:token", (req,res) -> {
            res.type("application/json");
            JSONObject getJSon = new JSONObject(req.body().toString());
            //String usernameReq = getJSon.getString("username");
            //String tokenReq = getJSon.getString("token");
            String usernameReq = req.params("username");//getJSon.getString("username");
            String tokenReq = req.params("token");//getJSon.getString("token");
            //System.out.println(usernameReq);
            if (service.getUser(usernameReq).getToken().equals(tokenReq)) {
                try {
                    URL url = new URL("http://localhost:8083/sites/"+req.params("id")+"/categories");
                    try {
                        URLConnection urlConnection = url.openConnection();
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                        if (urlConnection instanceof HttpURLConnection) {
                            HttpURLConnection connection = (HttpURLConnection) urlConnection;
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            Gson gsonSites = new Gson();
                            Site[] categories = gsonSites.fromJson(in, Site[].class);
                            return new Gson().toJsonTree(categories);
                        } else {
                            System.out.println("URL INVALIDA.");
                            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "URL INVALIDA"));
                        }
                    } catch (IOException exception) {
                        System.out.println(exception.getMessage());
                        return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, exception.getMessage()));
                    }
                } catch (MalformedURLException exception) {
                    System.out.println(exception.getMessage());
                    return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, exception.getMessage()));
                }
            } else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,"USUARIO NO EXISTE"));
            }
        });
    }
}