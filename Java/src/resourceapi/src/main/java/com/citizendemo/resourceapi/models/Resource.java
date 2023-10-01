package com.citizendemo.resourceapi.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "resources")
public class Resource {
    @Id
    public String internalId;
    public String resourceId;
    public String citizenId;
    public String name;
    public String status;
    @CreatedDate
    private Date dateCreated;
    @Version
    private Integer version;
    public Resource() {
    }

    public Resource(String resourceId, String citizenId, String name, String status) {
        this.resourceId = resourceId;
        this.citizenId = citizenId;
        this.name = name;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId='" + resourceId + '\'' +
                "citizenId='" + citizenId + '\'' +
                "name='" + name + '\'' +
                "status='" + status + '\'' +
                '}';
    }
}