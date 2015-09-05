package nl.mok.mastersofcode.service.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * ExceptionMapper that handles all exceptions that arise during REST calls. A
 * response will be built with the root exception message as plain text.
 * 
 * @author Jeroen Schepens
 */
@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(ExceptionUtils.getRootCauseMessage(exception))
				.type(MediaType.TEXT_PLAIN).build();
	}
}