//**********************************************************************************************
//                                       DocumentMetadataDTO.java 
//
// Author(s): Arnaud CHARLEROY, Morgane VIDAL
// PHIS-SILEX version 1.0
// Copyright © - INRA - 2017
// Creation date: March 2017
// Contact: arnaud.charleroy@inra.fr, morgane.vidal@inra.fr, anne.tireau@inra.fr, pascal.neveu@inra.fr
// Last modification date:  October, 12 2017 (Ajout du status de documents)
// Subject: Represents the submitted JSON for the documents
//***********************************************************************************************
package phis2ws.service.resources.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import phis2ws.service.resources.dto.manager.AbstractVerifiedClass;

public class DocumentMetadataDTO extends AbstractVerifiedClass {
    private String uri; // /!\ ne sera pas utilisé pour le POST de métadonnées
    private String documentType;
    private String checksum;
    private String creator;
    private String language; //Il est recommandé que la valeur suive la norme RFC4646
    private String title;
    private String creationDate;
    private String extension;
    private String comment;
    private List<ConcernItemDTO> concern; // Liste des éléments auxquels le doc est lié
    private String status; // Status du document (linked / unlinked). Linked quand l'objet auquel il est
                           // lié existe réellement, unlinked quand il ne l'est pas encore  
    
    private String serverFilePath; // ce champ n'est pas à fournir par le client. 
                                   // Sa valeur sera déterminée coté WS
    
    @Override
    public Map rules() {
        Map<String, Boolean> rules = new HashMap<>();
        rules.put("documentType", Boolean.TRUE);
        rules.put("checksum", Boolean.FALSE);
        rules.put("uri", Boolean.FALSE);
        rules.put("creator", Boolean.FALSE);
        rules.put("language", Boolean.FALSE);
        rules.put("title", Boolean.FALSE);
        rules.put("creationDate", Boolean.FALSE);
        rules.put("extension", Boolean.FALSE);
        rules.put("concern", Boolean.FALSE);
        rules.put("comment", Boolean.FALSE);
        rules.put("status", Boolean.TRUE);
        return rules;
    }

    @Override
    public Object createObjectFromDTO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @ApiModelProperty(example = "http://www.phenome-fppn.fr/vocabulary/2015#ScientificDocument")
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String mediaType) {
        this.documentType = mediaType;
    }
    
    @ApiModelProperty(example = "106fa487baa1728083747de1c6df73e9")
    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    
    @ApiModelProperty(example = "John Doe")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @ApiModelProperty(example = "fr")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    @ApiModelProperty(example = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(example = "2017-01-01")
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<ConcernItemDTO> getConcern() {
        return concern;
    }

    public void setConcern(List<ConcernItemDTO> concern) {
        this.concern = concern;
    }
    
    @ApiModelProperty(example = "jpg")
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getServerFilePath() {
        return serverFilePath;
    }

    public void setServerFilePath(String serverFilePath) {
        this.serverFilePath = serverFilePath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ApiModelProperty(example = "linked")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
