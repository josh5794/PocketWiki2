package pocketwiki.pocketwiki.com.pocketwiki2.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ENTITY".
*/
public class EntityDao extends AbstractDao<Entity, Long> {

    public static final String TABLENAME = "ENTITY";

    /**
     * Properties of entity Entity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property EntityId = new Property(0, Long.class, "entityId", true, "ENTITY_ID");
        public final static Property CreatedAt = new Property(1, String.class, "createdAt", false, "CREATED_AT");
        public final static Property UpdatedAt = new Property(2, String.class, "updatedAt", false, "UPDATED_AT");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property SubEntityArray = new Property(4, String.class, "subEntityArray", false, "SUB_ENTITY_ARRAY");
        public final static Property ImageURLThumb = new Property(5, String.class, "imageURLThumb", false, "IMAGE_URLTHUMB");
        public final static Property ImageURLLarge = new Property(6, String.class, "imageURLLarge", false, "IMAGE_URLLARGE");
        public final static Property ImageURLThumbOnline = new Property(7, String.class, "imageURLThumbOnline", false, "IMAGE_URLTHUMB_ONLINE");
        public final static Property ImageURLLargeOnline = new Property(8, String.class, "imageURLLargeOnline", false, "IMAGE_URLLARGE_ONLINE");
    };

    private DaoSession daoSession;


    public EntityDao(DaoConfig config) {
        super(config);
    }
    
    public EntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ENTITY\" (" + //
                "\"ENTITY_ID\" INTEGER PRIMARY KEY ," + // 0: entityId
                "\"CREATED_AT\" TEXT," + // 1: createdAt
                "\"UPDATED_AT\" TEXT," + // 2: updatedAt
                "\"NAME\" TEXT," + // 3: name
                "\"SUB_ENTITY_ARRAY\" TEXT," + // 4: subEntityArray
                "\"IMAGE_URLTHUMB\" TEXT," + // 5: imageURLThumb
                "\"IMAGE_URLLARGE\" TEXT," + // 6: imageURLLarge
                "\"IMAGE_URLTHUMB_ONLINE\" TEXT," + // 7: imageURLThumbOnline
                "\"IMAGE_URLLARGE_ONLINE\" TEXT);"); // 8: imageURLLargeOnline
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Entity entity) {
        stmt.clearBindings();
 
        Long entityId = entity.getEntityId();
        if (entityId != null) {
            stmt.bindLong(1, entityId);
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
 
        String subEntityArray = entity.getSubEntityArray();
        if (subEntityArray != null) {
            stmt.bindString(5, subEntityArray);
        }
 
        String imageURLThumb = entity.getImageURLThumb();
        if (imageURLThumb != null) {
            stmt.bindString(6, imageURLThumb);
        }
 
        String imageURLLarge = entity.getImageURLLarge();
        if (imageURLLarge != null) {
            stmt.bindString(7, imageURLLarge);
        }
 
        String imageURLThumbOnline = entity.getImageURLThumbOnline();
        if (imageURLThumbOnline != null) {
            stmt.bindString(8, imageURLThumbOnline);
        }
 
        String imageURLLargeOnline = entity.getImageURLLargeOnline();
        if (imageURLLargeOnline != null) {
            stmt.bindString(9, imageURLLargeOnline);
        }
    }

    @Override
    protected void attachEntity(Entity entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Entity readEntity(Cursor cursor, int offset) {
        Entity entity = new Entity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // entityId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // createdAt
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // updatedAt
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // subEntityArray
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // imageURLThumb
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // imageURLLarge
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // imageURLThumbOnline
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // imageURLLargeOnline
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Entity entity, int offset) {
        entity.setEntityId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreatedAt(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUpdatedAt(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSubEntityArray(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setImageURLThumb(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setImageURLLarge(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setImageURLThumbOnline(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setImageURLLargeOnline(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Entity entity, long rowId) {
        entity.setEntityId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Entity entity) {
        if(entity != null) {
            return entity.getEntityId();
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
