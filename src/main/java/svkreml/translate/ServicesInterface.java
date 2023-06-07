package svkreml.translate;

import svkreml.translate.dto.YaTranslateRequestDto;
import svkreml.translate.dto.YaTranslateResponseDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/translate/v2")
public interface ServicesInterface {


    @POST
    @Path("/translate")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    YaTranslateResponseDto translate(
            YaTranslateRequestDto yaTranslateRequestDto);


}
