package pocketwiki.pocketwiki.com.pocketwiki2;

/**
 * Created by chinmay on 8/4/16.
 */
public class EntityAndAreaInfo {

    private String areaName;
    private String entityName;
    private Long areaEntityId;
    private Long entityId;
    private String cityName;
    private String thumbURL;

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public boolean isContentExists() {
        return contentExists;
    }

    public void setContentExists(boolean contentExists) {
        this.contentExists = contentExists;
    }

    private boolean contentExists;

    public EntityAndAreaInfo(String areaName, String entityName, Long areaEntityId, Long entityId, String cityName, String thumbURL, String thumbURLOnline) {
        this.areaName = areaName;
        this.entityName = entityName;
        this.areaEntityId = areaEntityId;
        this.entityId = entityId;
        this.cityName = cityName;
        this.thumbURL = thumbURL;
        this.thumbURLOnline = thumbURLOnline;
    }

    private String thumbURLOnline;

    public String getThumbURLOnline() {
        return thumbURLOnline;
    }

    public void setThumbURLOnline(String thumbURLOnline) {
        this.thumbURLOnline = thumbURLOnline;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getAreaEntityId() {
        return areaEntityId;
    }

    public void setAreaEntityId(Long areaEntityId) {
        this.areaEntityId = areaEntityId;
    }
}
