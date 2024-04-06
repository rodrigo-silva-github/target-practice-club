package org.target.club.runner;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RestScannerRunner implements CommandLineRunner {

    private final RestClient restClient;

    public RestScannerRunner(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void run(String... args) throws Exception {

        List<ResourceKey> myPaths = new ArrayList<>();

        JsonNode response = restClient.get().uri("/v3/api-docs").retrieve().body(JsonNode.class);

        if (response != null) {
            JsonNode paths = response.get("paths");
            log.info("paths={}", paths);
            paths.properties().forEach(stringJsonNodeEntry -> {
                String key = stringJsonNodeEntry.getKey();
                stringJsonNodeEntry.getValue().properties().forEach(resourceVerb -> {
                    String resourceVerbKey = resourceVerb.getKey();
                    myPaths.add(new ResourceKey(key, resourceVerbKey));
                });
            });

        }


        log.info("myPaths={}", myPaths);

    }


}

record ResourceKey(String uri, String method) implements Serializable {
}