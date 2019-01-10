package com.xgx.dabainian;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PIC_INFO".
*/
public class PicInfoDao extends AbstractDao<PicInfo, Long> {

    public static final String TABLENAME = "PIC_INFO";

    /**
     * Properties of entity PicInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Pwd = new Property(1, String.class, "pwd", false, "PWD");
        public final static Property Pic = new Property(2, String.class, "pic", false, "PIC");
        public final static Property Createtime = new Property(3, String.class, "createtime", false, "CREATETIME");
    }


    public PicInfoDao(DaoConfig config) {
        super(config);
    }
    
    public PicInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PIC_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"PWD\" TEXT," + // 1: pwd
                "\"PIC\" TEXT," + // 2: pic
                "\"CREATETIME\" TEXT);"); // 3: createtime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PIC_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PicInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(2, pwd);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(3, pic);
        }
 
        String createtime = entity.getCreatetime();
        if (createtime != null) {
            stmt.bindString(4, createtime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PicInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(2, pwd);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(3, pic);
        }
 
        String createtime = entity.getCreatetime();
        if (createtime != null) {
            stmt.bindString(4, createtime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public PicInfo readEntity(Cursor cursor, int offset) {
        PicInfo entity = new PicInfo( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // pwd
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // pic
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // createtime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PicInfo entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setPwd(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPic(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCreatetime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PicInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PicInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PicInfo entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
