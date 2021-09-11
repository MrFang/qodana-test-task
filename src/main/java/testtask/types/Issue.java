package testtask.types;

public class Issue {
    public Project project;
    public String summary;
    public String description;

    public Issue () {}

    public Issue(Project project, String summary, String description) {
        this.project = project;
        this.summary = summary;
        this.description = description;
    }
}
