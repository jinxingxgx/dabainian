package com.xgx.dabainian;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xgx.dabainian.PicInfo;

import com.xgx.dabainian.PicInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig picInfoDaoConfig;

    private final PicInfoDao picInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        picInfoDaoConfig = daoConfigMap.get(PicInfoDao.class).clone();
        picInfoDaoConfig.initIdentityScope(type);

        picInfoDao = new PicInfoDao(picInfoDaoConfig, this);

        registerDao(PicInfo.class, picInfoDao);
    }
    
    public void clear() {
        picInfoDaoConfig.clearIdentityScope();
    }

    public PicInfoDao getPicInfoDao() {
        return picInfoDao;
    }

}
