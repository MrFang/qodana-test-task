package testtask.types;

import java.util.ArrayList;
import java.util.List;

public class Issue {
    public final Project project;
    public final String summary;
    public final String description;
    public final List<YouTrackCustomField> customFields;

    public Issue(Project project, String summary, String description) {
        this.project = project;
        this.summary = summary;
        this.description = description;
        customFields = new ArrayList<>();
    }

    public Issue(Project project, String summary, String description, List<YouTrackCustomField> customFields) {
        this.project = project;
        this.summary = summary;
        this.description = description;
        this.customFields = customFields;
    }
}
