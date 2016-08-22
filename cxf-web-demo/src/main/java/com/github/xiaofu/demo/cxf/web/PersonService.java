package com.github.xiaofu.demo.cxf.web;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
 
@Path("/personservice/")
@Produces({"application/xml","application/json"}) 
public interface PersonService extends Serializable {
    @GET
    @Path("/persons")
    public List<Person> getPersons();

    @GET
    @Path("/persons/{id}")
    public Person getPerson(@PathParam("id") String id);
}
