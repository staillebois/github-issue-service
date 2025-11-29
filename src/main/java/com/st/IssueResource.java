package com.st;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@Path("/api/issues")
public class IssueResource {

    @Inject
    IssueService issueService;

    @POST
    @Path("/fetch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchIssues(IssueRequest request) {
        try {
            java.util.List<IssueResponse> result = issueService.fetchIssues(request);
            return Response.ok(result).build();
        } catch (IOException e) {
            return Response.serverError().entity("Error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/pages")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPageCount(IssueRequest request) {
        try {
            PageCountResponse result = issueService.getIssuePageCount(request);
            return Response.ok(result).build();
        } catch (IOException e) {
            return Response.serverError().entity("Error: " + e.getMessage()).build();
        }
    }
}
