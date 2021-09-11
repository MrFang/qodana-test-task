package testtask.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$type", "shortName"})
public class Project {
    public String name;
    public String id;

    public Project setId(String id) {
        this.id = id;
        return this;
    }
}
