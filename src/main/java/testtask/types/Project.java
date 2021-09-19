package testtask.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$type", "shortName"})
public class Project {
    public String name;
    public String id;
}
