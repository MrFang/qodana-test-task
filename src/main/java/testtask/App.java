package testtask;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import testtask.types.Issue;
import testtask.types.Project;
import io.github.cdimascio.dotenv.Dotenv;
import testtask.types.YouTrackPriority;
import testtask.types.YouTrackType;

public class App {
    public final static String TARGET_PROJECT_NAME = "Demo project";

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        final String TOKEN = dotenv.get("TEST_TASK_TOKEN");
        final String API_URL = dotenv.get("TEST_TASK_API_URL");

        if (TOKEN == null) {
            App.exitWithErrorMessage("Environment variable TEST_TASK_TOKEN must be set");
        }

        if (API_URL == null) {
            App.exitWithErrorMessage("Environment variable TEST_TASK_API_URL must be set");
        }

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = null;

        try {
            request = HttpRequest.newBuilder()
                .uri(new URI(API_URL + "/admin/projects?fields=id,name"))
                .setHeader("Accept", "application/json")
                .setHeader("Authorization", "Bearer " + TOKEN)
                .setHeader("Content-Type", "application/json")
                .build();
        } catch (URISyntaxException e) {
            App.exitWithErrorMessage("Invalid API_URL: " + e.getMessage());
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            App.exitWithErrorMessage("Cannot fetch projects list: " + e.getMessage());
        }

        if (response == null) {
            App.exitWithErrorMessage("Cannot fetch projects list. Response is empty");
        }

        if (response.statusCode() != 200) {
            App.exitWithErrorMessage("Cannot fetch projects list. Status: " + response.statusCode());
        }

        Project targetProject = null;
        try {
            targetProject = mapper.readValue(response.body(), new TypeReference<List<Project>>() {})
                .stream()
                .filter(project -> project.name.equals(TARGET_PROJECT_NAME))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        } catch (JsonProcessingException e) {
            App.exitWithErrorMessage("Cannot deserialize projects list: " + e.getMessage());
        } catch (RuntimeException e) {
            App.exitWithErrorMessage("Cannot find project with name: " + TARGET_PROJECT_NAME);
        }

        try {
            request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(
                    new Issue(targetProject, "Summary", "Description", Arrays.asList(new YouTrackType("Bug"), new YouTrackPriority("Critical")))
                )))
                .uri(new URI(API_URL + "/issues"))
                .setHeader("Accept", "application/json")
                .setHeader("Authorization", "Bearer " + TOKEN)
                .setHeader("Content-Type", "application/json")
                .build();
        } catch (JsonProcessingException e) {
            App.exitWithErrorMessage("Cannot serialize Issue: " + e.getMessage());
        } catch (URISyntaxException e) {
            App.exitWithErrorMessage("Invalid API_URL: " + e.getMessage());
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            App.exitWithErrorMessage("Cannot create issue: " + e.getMessage());
        }

        if (response == null) {
            App.exitWithErrorMessage("Cannot create issue. Response is empty");
        }

        if (response.statusCode() != 200) {
            App.exitWithErrorMessage("Cannot create issue. Status code: " + response.statusCode());
        }

        System.out.println(response.body());
    }

    private static void exitWithErrorMessage(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
