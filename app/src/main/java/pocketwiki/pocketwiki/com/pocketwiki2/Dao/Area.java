package pocketwiki.pocketwiki.com.pocketwiki2.Dao;

import java.util.List;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "AREA".
 */
public class Area {

    private Long areaId;
    private String createdAt;
    private String updatedAt;
    private String name;
    private String cityName;
    private Long cityId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient AreaDao myDao;

    private City city;
    private Long city__resolvedKey;

    private List<AreaEntities> areaEntitiesList;

    public Area() {
    }

    public Area(Long areaId) {
        this.areaId = areaId;
    }

    public Area(Long areaId, String createdAt, String updatedAt, String name, String cityName, Long cityId) {
        this.areaId = areaId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.cityName = cityName;
        this.cityId = cityId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAreaDao() : null;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    /** To-one relationship, resolved on first access. */
    public City getCity() {
        Long __key = this.cityId;
        if (city__resolvedKey == null || !city__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CityDao targetDao = daoSession.getCityDao();
            City cityNew = targetDao.load(__key);
            synchronized (this) {
                city = cityNew;
            	city__resolvedKey = __key;
            }
        }
        return city;
    }

    public void setCity(City city) {
        synchronized (this) {
            this.city = city;
            cityId = city == null ? null : city.getCityId();
            city__resolvedKey = cityId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<AreaEntities> getAreaEntitiesList() {
        if (areaEntitiesList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AreaEntitiesDao targetDao = daoSession.getAreaEntitiesDao();
            List<AreaEntities> areaEntitiesListNew = targetDao._queryArea_AreaEntitiesList(areaId);
            synchronized (this) {
                if(areaEntitiesList == null) {
                    areaEntitiesList = areaEntitiesListNew;
                }
            }
        }
        return areaEntitiesList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetAreaEntitiesList() {
        areaEntitiesList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}