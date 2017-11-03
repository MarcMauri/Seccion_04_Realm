package app.android.mmauri.seccion_04_realm.models;

import java.util.Date;

import app.android.mmauri.seccion_04_realm.app.MyApplication;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by marc on 10/20/17.
 */

public class Note extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String description;
    @Required
    private Date createdAt;

    public Note() {}

    public Note(String description) {
        this.id = MyApplication.NoteID.incrementAndGet();
        this.description = description;
        createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
