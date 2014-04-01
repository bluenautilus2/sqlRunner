package org.bluenautilus.ldap.servlet;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


@Path("/echo")
public class JerseyEcho {

	@GET
	public Response getQuantify(@QueryParam(value="id") final String id) {

		String output = "Jersey say : " + id;

		return Response.status(200).entity(output).build();

	}

	@POST
	public Response updateQuantity(@PathParam("param") String msg) {

		String output = "Jersey say : " + msg;

		return Response.status(200).entity(output).build();

	}

	@DELETE
	public Response deleteQuantity(@PathParam("param") String msg) {

		String output = "Jersey say : " + msg;

		return Response.status(200).entity(output).build();

	}

	private void temp(){

	}

}
