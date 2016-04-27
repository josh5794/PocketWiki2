package pocketwiki.pocketwiki.com.pocketwiki2.Dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "AREA_ENTITIES".
 */
public class AreaEntities {

    private Long areaEntitiesId;
    private String createdAt;
    private String updatedAt;
    private Long areaId;
    private Long entityId;

    public AreaEntities() {
    }

    public AreaEntities(Long areaEntitiesId) {
        this.areaEntitiesId = areaEntitiesId;
    }

    public AreaEntities(Long areaEntitiesId, String createdAt, String updatedAt, Long areaId, Long entityId) {
        this.areaEntitiesId = areaEntitiesId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.areaId = areaId;
        this.entityId = entityId;
    }

    public Long getAreaEntitiesId() {
        return areaEntitiesId;
    }

    public void setAreaEntitiesId(Long areaEntitiesId) {
        this.areaEntitiesId = areaEntitiesId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
