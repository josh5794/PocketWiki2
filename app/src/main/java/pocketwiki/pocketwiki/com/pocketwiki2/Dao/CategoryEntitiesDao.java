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

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CategoryEntities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CATEGORY_ENTITIES".
*/
public class CategoryEntitiesDao extends AbstractDao<CategoryEntities, Long> {

    public static final String TABLENAME = "CATEGORY_ENTITIES";

    /**
     * Properties of entity CategoryEntities.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property CategoryEntitiesId = new Property(0, Long.class, "categoryEntitiesId", true, "CATEGORY_ENTITIES_ID");
        public final static Property CreatedAt = new Property(1, String.class, "createdAt", false, "CREATED_AT");
        public final static Property UpdatedAt = new Property(2, String.class, "updatedAt", false, "UPDATED_AT");
        public final static Property CategoryId = new Property(3, Long.class, "categoryId", false, "CATEGORY_ID");
        public final static Property EntityId = new Property(4, Long.class, "entityId", false, "ENTITY_ID");
    };

    private Query<CategoryEntities> category_CategoryEntitiesListQuery;
    private Query<CategoryEntities> entity_CategoryEntitiesListQuery;

    public CategoryEntitiesDao(DaoConfig config) {
        super(config);
    }
    
    public CategoryEntitiesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CATEGORY_ENTITIES\" (" + //
                "\"CATEGORY_ENTITIES_ID\" INTEGER PRIMARY KEY ," + // 0: categoryEntitiesId
                "\"CREATED_AT\" TEXT," + // 1: createdAt
                "\"UPDATED_AT\" TEXT," + // 2: updatedAt
                "\"CATEGORY_ID\" INTEGER," + // 3: categoryId
                "\"ENTITY_ID\" INTEGER);"); // 4: entityId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CATEGORY_ENTITIES\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CategoryEntities entity) {
        stmt.clearBindings();
 
        Long categoryEntitiesId = entity.getCategoryEntitiesId();
        if (categoryEntitiesId != null) {
            stmt.bindLong(1, categoryEntitiesId);
        }
 
        String createdAt = entity.getCreatedAt();
        if (createdAt != null) {
            stmt.bindString(2, createdAt);
        }
 
        String updatedAt = entity.getUpdatedAt();
        if (updatedAt != null) {
            stmt.bindString(3, updatedAt);
        }
 
        Long categoryId = entity.getCategoryId();
        if (categoryId != null) {
            stmt.bindLong(4, categoryId);
        }
 
        Long entityId = entity.getEntityId();
        if (entityId != null) {
            stmt.bindLong(5, entityId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CategoryEntities readEntity(Cursor cursor, int offset) {
        CategoryEntities entity = new CategoryEntities( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // categoryEntitiesId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // createdAt
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // updatedAt
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // categoryId
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // entityId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CategoryEntities entity, int offset) {
        entity.setCategoryEntitiesId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreatedAt(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUpdatedAt(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCategoryId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setEntityId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CategoryEntities entity, long rowId) {
        entity.setCategoryEntitiesId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CategoryEntities entity) {
        if(entity != null) {
            return entity.getCategoryEntitiesId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "categoryEntitiesList" to-many relationship of Category. */
    public List<CategoryEntities> _queryCategory_CategoryEntitiesList(Long categoryId) {
        synchronized (this) {
            if (category_CategoryEntitiesListQuery == null) {
                QueryBuilder<CategoryEntities> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CategoryId.eq(null));
                category_CategoryEntitiesListQuery = queryBuilder.build();
            }
        }
        Query<CategoryEntities> query = category_CategoryEntitiesListQuery.forCurrentThread();
        query.setParameter(0, categoryId);
        return query.list();
    }

    /** Internal query to resolve the "categoryEntitiesList" to-many relationship of Entity. */
    public List<CategoryEntities> _queryEntity_CategoryEntitiesList(Long entityId) {
        synchronized (this) {
            if (entity_CategoryEntitiesListQuery == null) {
                QueryBuilder<CategoryEntities> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.EntityId.eq(null));
                entity_CategoryEntitiesListQuery = queryBuilder.build();
            }
        }
        Query<CategoryEntities> query = entity_CategoryEntitiesListQuery.forCurrentThread();
        query.setParameter(0, entityId);
        return query.list();
    }

}