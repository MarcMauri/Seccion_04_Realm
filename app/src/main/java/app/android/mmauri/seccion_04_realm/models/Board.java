package app.android.mmauri.seccion_04_realm.models;

import java.util.Date;

import app.android.mmauri.seccion_04_realm.app.MyApplication;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by marc on 10/20/17.
 */

public class Board extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private Date createdAt;

    private RealmList<Note> notes;

    public Board() {}

    public Board(String title) {
        this.id = MyApplication.BoardID.incrementAndGet();
        this.title = title;
        this.createdAt = new Date();

        this.notes = new RealmList<Note>();
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }
}
