package pt.ubi.di.pdm.quizgame;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "level")
    private int level;
    @ColumnInfo(name = "Score")
    private int Score;
    @ColumnInfo(name = "Name")
    private String Name;

    public User(){
        this.Name = "";
        this.Score = 0;
        this.level = 0;
    }

    protected User(Parcel in) {
        id = in.readInt();
        level = in.readInt();
        Score = in.readInt();
        Name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getScore() {
        return Score;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return Name;
    }

    public void setScore(int score) {
        Score = score;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(level);
        parcel.writeInt(Score);
        parcel.writeString(Name);
    }
}
