package gcm.play.android.samples.com.gcmquickstart.notification;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.util.Range;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Neo on 2018/3/30.
 */

public class MyDefinitionReceiveMessage implements Parcelable {
    @SerializedName("fountain")
    private String mFountainName;
    @SerializedName("tag")
    private String mTagName;
    @SerializedName("device_id")
    private long mDeviceId;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("msgType")
    private int mMsgType;

    public String getFountainName() {
        return mFountainName;
    }

    public void setFountainName(String fountainName) {
        mFountainName = fountainName;
    }

    public String getPetTagName() {
        return mTagName;
    }

    public void setPetTagName(String tagName) {
        mTagName = tagName;
    }

    public long getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(long deviceId) {
        mDeviceId = deviceId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    @IntRange(from = 100, to = 299)
    public int getMsgType() {
        return mMsgType;
    }

    public void setMsgType(@IntRange(from = 100, to = 299) int msgType) {
        mMsgType = msgType;
    }

    public static final int MESSAGE_SHOP = 0;
    public static final int MESSAGE_FORECAST = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MESSAGE_SHOP, MESSAGE_FORECAST})
    public @interface MessageType {
    }

    public int getMsgRawType() {
        if (new Range<Integer>(100, 199).contains(mMsgType)) {
            return MESSAGE_SHOP;
        } else {
            return MESSAGE_FORECAST;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private MyDefinitionReceiveMessage(Parcel in) {
        this.mDeviceId = in.readInt();
        this.mFountainName = in.readString();
        this.mMessage = in.readString();
        this.mTagName = in.readString();
        this.mMsgType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mDeviceId);
        dest.writeString(this.mFountainName);
        dest.writeString(this.mMessage);
        dest.writeString(this.mTagName);
        dest.writeInt(this.mMsgType);
    }

    public static final Creator<MyDefinitionReceiveMessage> CREATOR = new Creator<MyDefinitionReceiveMessage>() {
        public MyDefinitionReceiveMessage createFromParcel(Parcel in) {
            return new MyDefinitionReceiveMessage(in);
        }

        public MyDefinitionReceiveMessage[] newArray(int size) {
            return new MyDefinitionReceiveMessage[size];
        }
    };
}
