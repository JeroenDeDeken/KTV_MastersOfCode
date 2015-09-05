package nl.mok.mastersofcode.service.rest;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Service used to authenticate and log out. Also used to log in as a guest.
 * 
 * @author Jeroen Schepens
 */
@Path("/security")
public class LoginService {

	private final static Logger LOGGER = Logger.getLogger(LoginService.class
			.getName());

	/**
	 * Authenticates based on provided credentials. The credentials are passed
	 * in the form of string containing the username and password seperated by a
	 * semicolon (;).
	 * 
	 * @param login
	 *            The provided credentials
	 * @param request
	 *            The HTTP request that calls this method
	 */
	@POST
	@Produces("text/plain")
	public void login(String login, @Context HttpServletRequest request) {
		try {
			String[] credentials = login.split(";");
			request.login(credentials[0], credentials[1]);
		} catch (ServletException sx) {
			LOGGER.log(WARNING, sx.getMessage());
		}
	}

	/**
	 * Logs in as a guest User. A guest User can see the spectator view.
	 * 
	 * @param request
	 *            The HTTP request that calls this method
	 */
	@GET
	@Path("/guest")
	@Produces("text/plain")
	public void loginAsGuest(@Context HttpServletRequest request) {
		LOGGER.log(INFO, "Guest logging in");
		try {
			request.login("guest", "guest");
		} catch (ServletException sx) {
			LOGGER.log(WARNING, sx.getMessage());
		}
	}

	/**
	 * Logs a user out.
	 * 
	 * @param request
	 *            The HTTP request that calls this method
	 */
	@GET
	@Produces("text/plain")
	public void logout(@Context HttpServletRequest request) {
		try {
			request.logout();
		} catch (ServletException sx) {
			LOGGER.log(WARNING, sx.getMessage());
		}
	}
}