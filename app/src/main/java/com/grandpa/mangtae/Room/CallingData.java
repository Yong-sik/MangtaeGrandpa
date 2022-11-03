package com.grandpa.mangtae.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class CallingData {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String writer;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String videoPath;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    @TypeConverters({Converters.class})
    @ColumnInfo(name = "created")
    public java.util.Date created;

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    public java.util.Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    @ColumnInfo(name = "name")
    public String name;

    public void setName(String name){
        this.name = name;
    }

    @ColumnInfo(name = "content")
    public String content;

    @TypeConverters({Converters.class})
    @ColumnInfo(name = "updateDate")
    public java.util.Date updateDate;

    @TypeConverters({Converters.class})
    @ColumnInfo(name = "deleteDate")
    public java.util.Date deleteDate;

    @ColumnInfo(name = "audioPath")
    public String audioPath;

    public String toString(){
        return "ToDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created='" + created + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", deleteDate='" + deleteDate + '\'' +
                ", category='" + category + '\'' +
                ", writer='" + writer + '\'' +
                ", audioPath='"+ audioPath + '\'' +
                '}' + "\n\n";
    }
}
