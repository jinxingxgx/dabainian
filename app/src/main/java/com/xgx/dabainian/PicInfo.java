package com.xgx.dabainian;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xgx on 2019/1/10 for dabainian
 */
@Entity
public class PicInfo implements Serializable, Parcelable {
    private static final long serialVersionUID = -4786328347279641586L;
    @Id
    private long id;
    @Property
    private String pwd;
    @Property
    private String pic;
    @Property
    private String createtime;

    protected PicInfo(Parcel in) {
        id = in.readLong();
        pwd = in.readString();
        pic = in.readString();
        createtime = in.readString();
    }

    public static final Creator<PicInfo> CREATOR = new Creator<PicInfo>() {
        @Override
        public PicInfo createFromParcel(Parcel in) {
            return new PicInfo(in);
        }

        @Override
        public PicInfo[] newArray(int size) {
            return new PicInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(pwd);
        dest.writeString(pic);
        dest.writeString(createtime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Generated(hash = 1225629502)
    public PicInfo(long id, String pwd, String pic, String createtime) {
        this.id = id;
        this.pwd = pwd;
        this.pic = pic;
        this.createtime = createtime;
    }

    public PicInfo() {
    }
}
