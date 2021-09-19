package testtask.types;

public class YouTrackType extends YouTrackCustomField {
    public final YouTrackCustomField value;

    public YouTrackType(String type) {
        super("SingleEnumIssueCustomField", "Type");
        this.value = new YouTrackCustomField("EnumBundleElement", type);
    }
}
