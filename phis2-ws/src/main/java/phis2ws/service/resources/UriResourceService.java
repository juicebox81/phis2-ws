//**********************************************************************************************
//                                       UriResourceService.java 
//
// Author(s): Eloan LAGIER
// PHIS-SILEX version 1.0
// Copyright © - INRA - 2018
// Creation date: 26 Feb 2018
// Contact: eloan.lagier@inra.fr, morgane.vidal@inra.fr, anne.tireau@inra.fr, pascal.neveu@inra.fr
// Last modification date:  26 Feb, 2018
// Subject: Represents the Uri Resource Service
//***********************************************************************************************
package phis2ws.service.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phis2ws.service.authentication.Session;
import phis2ws.service.configuration.DefaultBrapiPaginationValues;
import phis2ws.service.configuration.GlobalWebserviceValues;
import phis2ws.service.dao.sesame.UriDaoSesame;
import phis2ws.service.documentation.DocumentationAnnotation;
import phis2ws.service.injection.SessionInject;
import phis2ws.service.view.brapi.Status;
import phis2ws.service.view.brapi.form.ResponseFormAsk;
import phis2ws.service.view.brapi.form.ResponseFormUri;
import phis2ws.service.view.model.phis.Ask;
import phis2ws.service.view.model.phis.Uri;

@Api("/uri")
@Path("uri")
/**
 * Represents the Uri Resource Service
 *
 * @author Eloan LAGIER
 */
public class UriResourceService {

    final static Logger LOGGER = LoggerFactory.getLogger(UriResourceService.class);

    //user Session 
    @SessionInject
    Session userSession;

    private Response noResultFound(ResponseFormAsk response, ArrayList<Status> statusList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Response noResultFound(ResponseFormUri response, ArrayList<Status> statusList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * search if an uri is in the triplestore or not
     *
     * @param uri
     * @return Ask
     */
    @GET
    @Path("{uri}/exist")
    @ApiOperation(value = "ask if an URI is in the triplestore or not",
            notes = "Return a boolean")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "ask concept", response = Ask.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION),
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED),
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response isUriExisting(
            @ApiParam(value = DocumentationAnnotation.CONCEPT_URI_DEFINITION, required = true, example = DocumentationAnnotation.EXAMPLE_CONCEPT_URI) @QueryParam("Uri") String uri) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (uri != null) {
            uriDaoSesame.uri = uri;
        }

        uriDaoSesame.user = userSession.getUser();

        return getAskdata(uriDaoSesame);
    }

    /**
     * this GET return all the concept informations
     *
     * @param limit
     * @param page
     * @param uri
     * @return concept list. The result form depends on the query results e.g.
     * of result : { "metadata": { "pagination": null, "status": [],
     * "datafiles": [] }, "result": { "data": [ { "uri":
     * "http://www.phenome-fppn.fr/vocabulary/2017#Document", "infos": { "type":
     * "http://www.w3.org/2002/07/owl#Class", "label_en": "document",
     * "label_fr": "document" } } ] } }
     */
    @GET
    @Path("{uri}/concept")
    @ApiOperation(value = "Get all concept informations",
            notes = "Retrieve all infos in the limit of what we knows")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieve all experiments", response = Uri.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION),
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED),
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })

    @Produces(MediaType.APPLICATION_JSON)
    public Response getUriBySearch(
            @ApiParam(value = DocumentationAnnotation.PAGE_SIZE) @QueryParam("pageSize") @DefaultValue(DefaultBrapiPaginationValues.PAGE_SIZE) int limit,
            @ApiParam(value = DocumentationAnnotation.PAGE) @QueryParam("page") @DefaultValue(DefaultBrapiPaginationValues.PAGE) int page,
            @ApiParam(value = "Search by uri", example = DocumentationAnnotation.EXAMPLE_CONCEPT_URI) @QueryParam("uri") String uri) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (uri != null) {
            uriDaoSesame.uri = uri;
        }

        uriDaoSesame.user = userSession.getUser();
        uriDaoSesame.setPage(page);
        uriDaoSesame.setPageSize(limit);

        return getUriMetadata(uriDaoSesame);
    }

    /**
     * searche all uri with the label given
     *
     * @param limit
     * @param page
     * @param name
     * @return Response
     */
    @GET
    @Path("/{uri}/labels")
    @ApiOperation(value = "get all uri with this label",
            notes = "Retrieve all uri from the label given")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieve all labels", response = Uri.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION),
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED),
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })

    @Produces(MediaType.APPLICATION_JSON)
    public Response getLabelBySearch(
            @ApiParam(value = DocumentationAnnotation.PAGE_SIZE) @QueryParam("pageSize") @DefaultValue(DefaultBrapiPaginationValues.PAGE_SIZE) int limit,
            @ApiParam(value = DocumentationAnnotation.PAGE) @QueryParam("page") @DefaultValue(DefaultBrapiPaginationValues.PAGE) int page,
            @ApiParam(value = "Search by label", example = DocumentationAnnotation.EXAMPLE_CONCEPT_LABEL) @QueryParam("label") String name) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (name != null) {
            uriDaoSesame.uri = name;
        }

        uriDaoSesame.user = userSession.getUser();
        uriDaoSesame.setPage(page);
        uriDaoSesame.setPageSize(limit);

        return getLabelMetaData(uriDaoSesame);
    }

    @GET
    @Path("{uri}/instances")
    @ApiOperation(value = "Get all the instances of a concept",
            notes = "Retrieve all instances of subClass too, if deep=true")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieve instances", response = Uri.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION),
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED),
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInstancesList(
            @ApiParam(value = DocumentationAnnotation.CONCEPT_URI_DEFINITION, required = true, example = DocumentationAnnotation.EXAMPLE_CONCEPT_URI) @QueryParam("conceptUri") String uri,
            @ApiParam(value = DocumentationAnnotation.DEEP) @QueryParam("deep") @DefaultValue(DocumentationAnnotation.EXAMPLE_DEEP) String deep,
            @ApiParam(value = DocumentationAnnotation.PAGE_SIZE) @QueryParam("pageSize") @DefaultValue(DefaultBrapiPaginationValues.PAGE_SIZE) int limit,
            @ApiParam(value = DocumentationAnnotation.PAGE) @QueryParam("page") @DefaultValue(DefaultBrapiPaginationValues.PAGE) int page) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (uri != null) {
            uriDaoSesame.uri = uri;
        }
        if (deep != null) {
            uriDaoSesame.deep = Boolean.valueOf(deep);
        }
        uriDaoSesame.setPageSize(limit);
        uriDaoSesame.setPage(page);
        uriDaoSesame.user = userSession.getUser();

        return getInstancesData(uriDaoSesame);
    }

    /**
     * give all the class parent of the uri
     *
     * @param uri
     * @param limit
     * @param page
     * @return { "metadata": { "pagination": null, "status": [], "datafiles": []
     * }, "result": { "data": [ { "uri":
     * "http://www.phenome-fppn.fr/vocabulary/2017#Document", "properties": {} }
     * ] } }
     */
    @GET
    @Path("{uri}/ancestors")
    @ApiOperation(value = "Get all the ancestor of an uri",
            notes = "Retrieve all Class parents of the uri")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieve Concept", response = Uri.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION),
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED),
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAncestorsList(
            @ApiParam(value = DocumentationAnnotation.CONCEPT_URI_DEFINITION, required = true, example = DocumentationAnnotation.EXAMPLE_CONCEPT_URI) @QueryParam("Uri") String uri,
            @ApiParam(value = DocumentationAnnotation.PAGE_SIZE) @QueryParam("pageSize") @DefaultValue(DefaultBrapiPaginationValues.PAGE_SIZE) int limit,
            @ApiParam(value = DocumentationAnnotation.PAGE) @QueryParam("page") @DefaultValue(DefaultBrapiPaginationValues.PAGE) int page) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (uri != null) {
            uriDaoSesame.uri = uri;
        }
        uriDaoSesame.setPageSize(limit);
        uriDaoSesame.setPage(page);
        uriDaoSesame.user = userSession.getUser();

        return getAncestorsMetaData(uriDaoSesame);
    }

    /**
     * give all the concepts with the same parent
     *
     * @param uri
     * @param limit
     * @param page
     * @return { "metadata": { "pagination": { "pageSize": 20, "currentPage": 0,
     * "totalCount": 11, "totalPages": 1 }, "status": [], "datafiles": [] },
     * "result": { "data": [ { "uri":
     * "http://www.phenome-fppn.fr/vocabulary/2017#DataFile", "properties": {}
     * }, { "uri":
     * "http://www.phenome-fppn.fr/vocabulary/2017#TechnicalDocument",
     * "properties": {} } ] } }
     */
    @GET
    @Path("{uri}/siblings")
    @ApiOperation(value = "Get all the siblings of an Uri",
            notes = "Retrieve all Class with same parent")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieve Concept", response = Uri.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION),
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED),
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSibblingsList(
            @ApiParam(value = DocumentationAnnotation.CONCEPT_URI_DEFINITION, required = true, example = DocumentationAnnotation.EXAMPLE_SIBLING_URI) @QueryParam("Uri") String uri,
            @ApiParam(value = DocumentationAnnotation.PAGE_SIZE) @QueryParam("pageSize") @DefaultValue(DefaultBrapiPaginationValues.PAGE_SIZE) int limit,
            @ApiParam(value = DocumentationAnnotation.PAGE) @QueryParam("page") @DefaultValue(DefaultBrapiPaginationValues.PAGE) int page) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (uri != null) {
            uriDaoSesame.uri = uri;
        }
        uriDaoSesame.setPageSize(limit);
        uriDaoSesame.setPage(page);
        uriDaoSesame.user = userSession.getUser();

        return getSiblingsMetaData(uriDaoSesame);
    }

    /**
     * search all descendants of a given uri
     *
     * @param uri
     * @param limit
     * @param page
     * @return { "metadata": { "pagination": { "pageSize": 20, "currentPage": 0,
     * "totalCount": 12, "totalPages": 1 }, "status": [], "datafiles": [] },
     * "result": { "data": [ { "uri":
     * "http://www.phenome-fppn.fr/vocabulary/2017#Document", "properties": {}
     * }, { "uri": "http://www.phenome-fppn.fr/vocabulary/2017#DataFile",
     * "properties": {} }, { "uri":
     * "http://www.phenome-fppn.fr/vocabulary/2017#TechnicalDocument",
     * "properties": {} } ] } }
     */
    @GET
    @Path("{uri}/descendants")
    @ApiOperation(value = "Get all the descendants of an uri",
            notes = "Retrieve all subclass and the subClass of subClass too")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieve Concept", response = Uri.class, responseContainer = "List")
        ,
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION)
        ,
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED)
        ,
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDescendantsList(
            @ApiParam(value = DocumentationAnnotation.CONCEPT_URI_DEFINITION, required = true, example = DocumentationAnnotation.EXAMPLE_CONCEPT_URI) @QueryParam("conceptUri") String uri,
            @ApiParam(value = DocumentationAnnotation.PAGE_SIZE) @QueryParam("pageSize") @DefaultValue(DefaultBrapiPaginationValues.PAGE_SIZE) int limit,
            @ApiParam(value = DocumentationAnnotation.PAGE) @QueryParam("page") @DefaultValue(DefaultBrapiPaginationValues.PAGE) int page) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (uri != null) {
            uriDaoSesame.uri = uri;
        }
        uriDaoSesame.setPageSize(limit);
        uriDaoSesame.setPage(page);
        uriDaoSesame.user = userSession.getUser();

        return getDescendantsMetaData(uriDaoSesame);
    }

    /**
     * return the type of an uri if exist else return empty list
     *
     * @param uri
     * @return false or type of the uri
     */
    @GET
    @Path("{uri}/type")
    @ApiOperation(value = "get the type of an uri if exist",
            notes = "else it will return empty list")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieve type", response = Uri.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = DocumentationAnnotation.BAD_USER_INFORMATION),
        @ApiResponse(code = 401, message = DocumentationAnnotation.USER_NOT_AUTHORIZED),
        @ApiResponse(code = 500, message = DocumentationAnnotation.ERROR_FETCH_DATA)
    })

    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", required = true,
                dataType = "string", paramType = "header",
                value = DocumentationAnnotation.ACCES_TOKEN,
                example = GlobalWebserviceValues.AUTHENTICATION_SCHEME + " ")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeIfUriExist(
            @ApiParam(value = DocumentationAnnotation.CONCEPT_URI_DEFINITION, required = true, example = DocumentationAnnotation.EXAMPLE_CONCEPT_URI) @QueryParam("conceptUri") String uri) {

        UriDaoSesame uriDaoSesame = new UriDaoSesame();
        if (uri != null) {
            uriDaoSesame.uri = uri;
        }

        uriDaoSesame.user = userSession.getUser();

        return getUriTypedata(uriDaoSesame);
    }

    /**
     * collect all the data for the instances request
     *
     * @param uriDaoSesame
     * @return Response
     */
    private Response getInstancesData(UriDaoSesame uriDaoSesame) {
        ArrayList<Uri> uris;
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormUri getResponse;

        uris = uriDaoSesame.instancesPaginate();
        if (uris == null) { //no result found
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);
        } else if (!uris.isEmpty()) { //result founded
            getResponse = new ResponseFormUri(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), uris, false);
            if (getResponse.getResult().dataSize() == 0) { //no result found
                return noResultFound(getResponse, statusList);
            } else { //return instances metadata
                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else { //no result found
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);
        }
    }

    /**
     * collect all the data for the ask request
     *
     * @param conceptDaoSesame
     * @return Response
     */
    private Response getAskdata(UriDaoSesame uriDaoSesame) {
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormAsk getResponse;
        ArrayList<Ask> ask = uriDaoSesame.askUriExistance();
        if (ask == null) {//no result found
            getResponse = new ResponseFormAsk(0, 0, ask, true);
            return noResultFound(getResponse, statusList);
        } else if (!ask.isEmpty()) {// result founded
            getResponse = new ResponseFormAsk(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), ask, false);
            if (getResponse.getResult().dataSize() == 0) { //no result 
                return noResultFound(getResponse, statusList);
            } else { // return ask metadata
                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else { //no result found
            getResponse = new ResponseFormAsk(0, 0, ask, true);
            return noResultFound(getResponse, statusList);
        }
    }

    /**
     * collect all the data of the user request
     *
     * @param uriDaoSesame
     * @return Response
     */
    private Response getUriMetadata(UriDaoSesame uriDaoSesame) {
        ArrayList<Uri> uris;
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormUri getResponse;

        uris = uriDaoSesame.allPaginate();

        if (uris == null) { //no result found
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);
        } else if (!uris.isEmpty()) { //concepts founded
            getResponse = new ResponseFormUri(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), uris, false);
            if (getResponse.getResult().dataSize() == 0) { // no result found
                return noResultFound(getResponse, statusList);
            } else { //return concepts metadata
                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else { //no result found
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);
        }
    }

    /**
     * getLabelMetaData:
     *
     * @param uriDaoSesame collect all the Label data
     */
    private Response getLabelMetaData(UriDaoSesame uriDaoSesame) {
        ArrayList<Uri> uris;
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormUri getResponse;

        uris = uriDaoSesame.labelsPaginate();
        if (uris == null) {
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);
        } else if (!uris.isEmpty()) {
            getResponse = new ResponseFormUri(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), uris, false);
            if (getResponse.getResult().dataSize() == 0) {
                return noResultFound(getResponse, statusList);
            } else {
                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else {
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);

        }
    }

    /**
     * collect all the data for the ancestors request
     *
     * @param uriDaoSesame
     * @return Response
     */
    private Response getAncestorsMetaData(UriDaoSesame uriDaoSesame) {
        ArrayList<Uri> concepts;
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormUri getResponse;

        concepts = uriDaoSesame.AncestorsAllPaginate();
        if (concepts == null) { //no result found
            getResponse = new ResponseFormUri(0, 0, concepts, true);
            return noResultFound(getResponse, statusList);
        } else if (!concepts.isEmpty()) { //result founded
            getResponse = new ResponseFormUri(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), concepts, false);
            if (getResponse.getResult().dataSize() == 0) { //no result found
                return noResultFound(getResponse, statusList);
            } else { //return concepts metadata
                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else { // no result found
            getResponse = new ResponseFormUri(0, 0, concepts, true);
            return noResultFound(getResponse, statusList);
        }
    }

    /**
     * collect all the data for the siblings request
     *
     * @param uriDaoSesame
     * @return Response
     */
    private Response getSiblingsMetaData(UriDaoSesame uriDaoSesame) {
        ArrayList<Uri> concepts;
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormUri getResponse;

        concepts = uriDaoSesame.SiblingsAllPaginate();
        if (concepts == null) { //no result found
            getResponse = new ResponseFormUri(0, 0, concepts, true);
            return noResultFound(getResponse, statusList);
        } else if (!concepts.isEmpty()) { //result founded
            getResponse = new ResponseFormUri(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), concepts, false);
            if (getResponse.getResult().dataSize() == 0) { //no result founded
                return noResultFound(getResponse, statusList);
            } else { //return concepts metadata

                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else { //no result found
            getResponse = new ResponseFormUri(0, 0, concepts, true);
            return noResultFound(getResponse, statusList);
        }
    }

    /**
     * collect all the data for the descendant request
     *
     * @param uriDaoSesame
     * @return Response
     */
    private Response getDescendantsMetaData(UriDaoSesame uriDaoSesame) {
        ArrayList<Uri> concepts;
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormUri getResponse;

        concepts = uriDaoSesame.descendantsAllPaginate();
        if (concepts == null) { //no result found
            getResponse = new ResponseFormUri(0, 0, concepts, true);
            return noResultFound(getResponse, statusList);
        } else if (!concepts.isEmpty()) { //concept founded 
            getResponse = new ResponseFormUri(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), concepts, false);
            if (getResponse.getResult().dataSize() == 0) { // no result found
                return noResultFound(getResponse, statusList);
            } else {//return concepts metadata
                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else { // no result found
            getResponse = new ResponseFormUri(0, 0, concepts, true);
            return noResultFound(getResponse, statusList);
        }
    }

    /**
     * collect all the data of the ask/type request
     *
     * @param uriDaoSesame
     * @return Response
     */
    private Response getUriTypedata(UriDaoSesame uriDaoSesame) {
        ArrayList<Uri> uris;
        ArrayList<Status> statusList = new ArrayList<>();
        ResponseFormUri getResponse;
        uris = uriDaoSesame.getAskTypeAnswer();
        if (uris == null) {//no result found
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);
        } else if (!uris.isEmpty()) { //result founded
            getResponse = new ResponseFormUri(uriDaoSesame.getPageSize(), uriDaoSesame.getPage(), uris, false);
            if (getResponse.getResult().dataSize() == 0) {//no result found
                return noResultFound(getResponse, statusList);
            } else { //return meta data
                getResponse.setStatus(statusList);
                return Response.status(Response.Status.OK).entity(getResponse).build();
            }
        } else {//no result found
            getResponse = new ResponseFormUri(0, 0, uris, true);
            return noResultFound(getResponse, statusList);
        }
    }
}
