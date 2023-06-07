package svkreml.translate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import svkreml.translate.dto.YaTranslateRequestDto;
import svkreml.translate.dto.YaTranslateResponseDto;
import svkreml.translate.translateDto.ApimPublisherDto;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class TranslateMain {
    private static final String API_KEY = "----";
    private static final String FOLDER_ID = "----";
    private static final String SOURCE_LANGUAGE_CODE = "en";
    private static final String TARGET_LANGUAGE = "ru";

    public static void main(String[] args) throws Exception {


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        final ApimPublisherDto apimPublisherDto = mapper.readValue(new FileInputStream("tests/en.json"), ApimPublisherDto.class);


        Set<String> tokens = new HashSet<>();

        for (Map.Entry<String, List<ApimPublisherDto.Translate>> stringTranslateEntry : apimPublisherDto.entrySet()) {
            final List<ApimPublisherDto.Translate> translates = stringTranslateEntry.getValue();


            for (ApimPublisherDto.Translate translate : translates) {
                tokens.add(translate.getValue());
                if (translate.getChildren() != null) {
                    for (ApimPublisherDto.Translate child : translate.getChildren()) {
                        tokens.add(child.getValue());
                    }
                }
            }

        }


        ServicesInterface proxy = getServicesInterface();
        final ArrayList<String> tokenList = new ArrayList<>(tokens);

        HashMap<String, String> translateMap = new HashMap<>();

        int page = 0;
        int size = 100;
        int processed = 0;
        while (true) {
            final int from = page * size;
            int to = (page + 1) * size;
            if (to > tokenList.size()) {
                to = tokenList.size();
            }
            final List<String> subList = tokenList.subList(from, to);

            translatePage(proxy, subList, translateMap);
            processed += subList.size();
            if (processed == tokenList.size()) break;
            page++;
        }


        mapper.writeValue(new File("output.json"), translateMap);
    }

    private static void translatePage(ServicesInterface proxy, List<String> tokenList, HashMap<String, String> translateMap) {
        YaTranslateRequestDto yaTranslateRequestDto = new YaTranslateRequestDto(FOLDER_ID, tokenList, SOURCE_LANGUAGE_CODE, TARGET_LANGUAGE);
        YaTranslateResponseDto yaTranslateResponseDto = proxy.translate(yaTranslateRequestDto);

        for (int i = 0; i < tokenList.size(); i++) {
            translateMap.put(tokenList.get(i), yaTranslateResponseDto.getTranslations().get(i).getText());
        }
/*        for (int i = 0; i < tokenList.size(); i++) {
            translateMap.put(tokenList.get(i), tokenList.get(i));
        }*/
    }

    private static ServicesInterface getServicesInterface() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        cm.setMaxTotal(200); // Increase max total connection to 200
        cm.setDefaultMaxPerRoute(20); // Increase default max connection per route to 20
        ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(httpClient);


        ResteasyClient client = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).httpEngine(engine).build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath("https://translate.api.cloud.yandex.net"));
        target.register(new ApiKeyAuth(API_KEY));


        return target.proxy(ServicesInterface.class);
    }
}
