package com.connectto.general.web.rest.provider;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.lcp.ResponseStatus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created with IntelliJ IDEA.
 * User: Serozh
 * Date: 05.05.13
 * Time: 19:53
 * To change this template use File | Settings | File Templates.
 */
@Provider
public class InternalException implements ExceptionMapper<InternalErrorException> {

    @Override
    public Response toResponse(InternalErrorException exception) {
        ResponseDto dto = new ResponseDto();
        dto.setStatus(ResponseStatus.INTERNAL_ERROR);
        dto.addMessage("The internal error is occurred [ " + exception.getMessage() + " ]");
        System.out.println("InternalException ");
        return Response.
                serverError().
                status(Response.Status.OK).
                type(MediaType.APPLICATION_JSON).
                entity(dto).
                build();
    }
}
