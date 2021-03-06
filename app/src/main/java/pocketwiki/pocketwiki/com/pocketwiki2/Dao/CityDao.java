package pocketwiki.pocketwiki.com.pocketwiki2.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.City;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CITY".
*/
public class CityDao extends AbstractDao<City, Long> {

    public static final String TABLENAME = "CITY";

    /**
     * Properties of entity City.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property CityId = new Property(0, Long.class, "cityId", true, "CITY_ID");
        public final static Property CreatedAt = new Property(1, String.class, "createdAt", false, "CREATED_AT");
        public final static Property UpdatedAt = new Property(2, String.class, "updatedAt", false, "UPDATED_AT");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property EntityCount = new Property(4, Integer.class, "entityCount", false, "ENTITY_COUNT");
    };


    public CityDao(DaoConfig config) {
        super(config);
    }
    
    public CityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CITY\" (" + //
                "\"CITY_ID\" INTEGER PRIMARY KEY ," + // 0: cityId
                "\"CREATED_AT\" TEXT," + // 1: createdAt
                "\"UPDATED_AT\" TEXT," + // 2: updatedAt
                "\"NAME\" TEXT," + // 3: name
                "\"ENTITY_COUNT\" INTEGER);"); // 4: entityCount
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, City entity) {
        stmt.clearBindings();
 
        Long cityId = entity.getCityId();
        if (cityId != null) {
            stmt.bindLong(1, cityId);
        }
 
        String createdAt = entity.getCreatedAt();
        if (createdAt != null) {
            stmt.bindString(2, createdAt);
        }
 
        String updatedAt = entity.getUpdatedAt();
        if (updatedAt != null) {
            stmt.bindString(3, updatedAt);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        Integer entityCount = entity.getEntityCount();
        if (entityCount != null) {
            stmt.bindLong(5, entityCount);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public City readEntity(Cursor cursor, int offset) {
        City entity = new City( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // cityId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // createdAt
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // updatedAt
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // entityCount
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, City entity, int offset) {
        entity.setCityId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreatedAt(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUpdatedAt(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEntityCount(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(City entity, long rowId) {
        entity.setCityId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(City entity) {
        if(entity != null) {
            return entity.getCityId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
