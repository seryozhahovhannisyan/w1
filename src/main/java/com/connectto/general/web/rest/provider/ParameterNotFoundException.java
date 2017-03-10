package com.connectto.general.web.rest.provider;

import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.lcp.ResponseStatus;
import com.sun.jersey.api.ParamException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created with IntelliJ IDEA.
 * User: Serozh
 * Date: 05.05.13
 * Time: 19:58
 * To change this template use File | Settings | File Templates.
 */

@Provider
public class ParameterNotFoundException implements ExceptionMapper<ParamException> {
    @Override
    public Response toResponse(ParamException exception) {

        ResponseDto dto = new ResponseDto();
        dto.setStatus(ResponseStatus.INVALID_PARAMETER);
        dto.addMessage("The service has not like params [ " + exception.toString() + " ]");
        System.out.println("ParameterNotFoundException ");
        return Response.
                status(Response.Status.OK).
                type(MediaType.APPLICATION_JSON).
                entity(dto).
                build();
    }
}
