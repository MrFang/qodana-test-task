package testtask.types;

public class YouTrackPriority extends YouTrackCustomField {
    public final YouTrackCustomField value;
    public YouTrackPriority(String priority) {
        super("SingleEnumIssueCustomField", "Priority");
        this.value = new YouTrackCustomField("EnumBundleElement", priority);
    }
}
