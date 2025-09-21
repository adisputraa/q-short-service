package org.github.adisputraa.qshort.api;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.github.adisputraa.qshort.data.UrlMapping;

import java.net.URI;
import java.util.UUID;

@Path("/urls")
public class UrlResource {

    // DTO (Data Transfer Object) sebagai inner class untuk request body.
    public static class UrlRequest {
        public String url;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(UrlRequest request) {
        if (request.url == null || request.url.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"URL cannot be empty\"}")
                    .build();
        }

        // logika untuk membuat url yang unik
        String shortCode = UUID.randomUUID().toString().substring(0,7);

        UrlMapping mapping = new UrlMapping();
        mapping.originalUrl = request.url;
        mapping.shortCode = shortCode;

        // Metode persist() dari PanacheEntity
        mapping.persist();

        return Response.status(Response.Status.CREATED).entity(mapping).build();
    }

    @GET
    @Path("/{shortCode}")
    public Response redirect(@PathParam("shortCode") String shortCode) {
        UrlMapping mapping = UrlMapping.findByShortCode(shortCode);

        if (mapping == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Mengirimkan respons HTTP 303 See Other untuk redirect
        return Response.seeOther(URI.create(mapping.originalUrl)).build();
    }
}
