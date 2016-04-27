package pocketwiki.pocketwiki.com.pocketwiki2.Dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.SubEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SUB_ENTITY".
*/
public class SubEntityDao extends AbstractDao<SubEntity, Long> {

    public static final String TABLENAME = "SUB_ENTITY";

    /**
     * Properties of entity SubEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property SubEntityId = new Property(0, Long.class, "subEntityId", true, "SUB_ENTITY_ID");
        public final static Property CreatedAt = new Property(1, String.class, "createdAt", false, "CREATED_AT");
        public final static Property UpdatedAt = new Property(2, String.class, "updatedAt", false, "UPDATED_AT");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property EntityId = new Property(4, Long.class, "entityId", false, "ENTITY_ID");
        public final static Property ImageURLThumb = new Property(5, String.class, "imageURLThumb", false, "IMAGE_URLTHUMB");
        public final static Property ImageURLLarge = new Property(6, String.class, "imageURLLarge", false, "IMAGE_URLLARGE");
        public final static Property ImageURLThumbOnline = new Property(7, String.class, "imageURLThumbOnline", false, "IMAGE_URLTHUMB_ONLINE");
        public final static Property ImageURLLargeOnline = new Property(8, String.class, "imageURLLargeOnline", false, "IMAGE_URLLARGE_ONLINE");
    };

    private Query<SubEntity> entity_SubEntityListQuery;

    public SubEntityDao(DaoConfig config) {
        super(config);
    }
    
    public SubEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SUB_ENTITY\" (" + //
                "\"SUB_ENTITY_ID\" INTEGER PRIMARY KEY ," + // 0: subEntityId
                "\"CREATED_AT\" TEXT," + // 1: createdAt
                "\"UPDATED_AT\" TEXT," + // 2: updatedAt
                "\"NAME\" TEXT," + // 3: name
                "\"ENTITY_ID\" INTEGER," + // 4: entityId
                "\"IMAGE_URLTHUMB\" TEXT," + // 5: imageURLThumb
                "\"IMAGE_URLLARGE\" TEXT," + // 6: imageURLLarge
                "\"IMAGE_URLTHUMB_ONLINE\" TEXT," + // 7: imageURLThumbOnline
                "\"IMAGE_URLLARGE_ONLINE\" TEXT);"); // 8: imageURLLargeOnline
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SUB_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SubEntity entity) {
        stmt.clearBindings();
 
        Long subEntityId = entity.getSubEntityId();
        if (subEntityId != null) {
            stmt.bindLong(1, subEntityId);
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
 
        Long entityId = entity.getEntityId();
        if (entityId != null) {
            stmt.bindLong(5, entityId);
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

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SubEntity readEntity(Cursor cursor, int offset) {
        SubEntity entity = new SubEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // subEntityId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // createdAt
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // updatedAt
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // entityId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // imageURLThumb
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // imageURLLarge
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // imageURLThumbOnline
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // imageURLLargeOnline
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SubEntity entity, int offset) {
        entity.setSubEntityId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreatedAt(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUpdatedAt(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEntityId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setImageURLThumb(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setImageURLLarge(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setImageURLThumbOnline(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setImageURLLargeOnline(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SubEntity entity, long rowId) {
        entity.setSubEntityId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SubEntity entity) {
        if(entity != null) {
            return entity.getSubEntityId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "subEntityList" to-many relationship of Entity. */
    public List<SubEntity> _queryEntity_SubEntityList(Long entityId) {
        synchronized (this) {
            if (entity_SubEntityListQuery == null) {
                QueryBuilder<SubEntity> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.EntityId.eq(null));
                entity_SubEntityListQuery = queryBuilder.build();
            }
        }
        Query<SubEntity> query = entity_SubEntityListQuery.forCurrentThread();
        query.setParameter(0, entityId);
        return query.list();
    }

}