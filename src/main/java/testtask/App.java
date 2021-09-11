package testtask;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import testtask.types.Issue;
import testtask.types.Project;
import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public final static String TARGET_PROJECT_NAME = "Demo project";

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Dotenv dotenv = Dotenv.load();
        final String TOKEN = dotenv.get("TEST_TASK_TOKEN");
        final String API_URL = dotenv.get("TEST_TASK_API_URL");

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(API_URL + "/admin/projects?fields=id,name"))
            .setHeader("Accept", "application/json")
            .setHeader("Authorization", "Bearer " + TOKEN)
            .setHeader("Content-Type", "application/json")
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String projectId = mapper.readValue(response.body(), new TypeReference<List<Project>>() {})
            .stream()
            .filter(project -> project.name.equals(TARGET_PROJECT_NAME))
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .id;

        request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(
                new Issue(
                    (new Project()).setId(projectId),
                    "sum1",
                    "desc1")
            )))
            .uri(new URI(API_URL + "/issues"))
            .setHeader("Accept", "application/json")
            .setHeader("Authorization", "Bearer " + TOKEN)
            .setHeader("Content-Type", "application/json")
            .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
