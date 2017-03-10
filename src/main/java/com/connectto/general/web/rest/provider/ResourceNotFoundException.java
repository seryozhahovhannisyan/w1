package com.connectto.general.web.rest.provider;

import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.lcp.ResponseStatus;
import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created with IntelliJ IDEA.
 * User: Serozh
 * Date: 05.05.13
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */

@Provider
public class ResourceNotFoundException implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        response.addMessage("Resource not found [ " + exception.getMessage() + " ]");
        System.out.println("ResourceNotFoundException ");
        return Response.
                status(Response.Status.OK).
                entity(response).
                type(MediaType.APPLICATION_JSON).
                build();
    }
}
