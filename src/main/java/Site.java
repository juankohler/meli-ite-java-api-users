public class Site {

    private String id;
    private String name;

    public Site() { // el constructor por defecto lo usa el compilador si no creo ningun constructor
    }

    public Site(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
