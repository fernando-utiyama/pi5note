package univesp.pi5note.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
public class AwsService {

    @Value("${aws.url}")
    private String urlAws;

    private RestTemplate rest;

    public AwsService(RestTemplateBuilder restTemplateBuilder) {
        this.rest = restTemplateBuilder.build();
    }

    public List<RequisicaoDTO> getCommands() {
        String url = UriComponentsBuilder.fromHttpUrl(urlAws)
                .path("/requests").toUriString();
        ResponseEntity<List<RequisicaoDTO>> entity = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RequisicaoDTO>>() {
        });
        return entity.getBody();
    }

    public void postResponse(RequisicaoDTO requisicao) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = UriComponentsBuilder.fromHttpUrl(urlAws)
                .queryParam("id", requisicao.getId())
                .path("/response").toUriString();
        rest.exchange(url, HttpMethod.GET, new HttpEntity<>(requisicao, headers), RequisicaoDTO.class);
    }
}
