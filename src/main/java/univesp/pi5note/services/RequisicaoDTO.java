package univesp.pi5note.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequisicaoDTO {

    @JsonProperty
    Long id;

    @JsonProperty
    String command;

    @JsonProperty
    String medidas;

    @JsonProperty
    String status;

}
