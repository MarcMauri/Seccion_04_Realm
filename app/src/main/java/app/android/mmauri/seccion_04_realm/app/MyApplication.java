package app.android.mmauri.seccion_04_realm.app;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

import app.android.mmauri.seccion_04_realm.models.Board;
import app.android.mmauri.seccion_04_realm.models.Note;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by marc on 10/20/17.
 */

public class MyApplication extends Application {

    public static AtomicInteger BoardID = new AtomicInteger();
    public static AtomicInteger NoteID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();

        setUpRealmConfig();

        Realm realm = Realm.getDefaultInstance();
        BoardID = getIdByTable(realm, Board.class);
        NoteID = getIdByTable(realm, Note.class);
        realm.close();

    }

    /* Configuracion basica de Realm */
    private void setUpRealmConfig() {
        RealmConfiguration config = new RealmConfiguration
                .Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass) {
        /* Recuperamos la tabla del objeto "anyClass" */
        RealmResults<T> results = realm.where(anyClass).findAll();
        /* Devolvemos el "id" maximo de la tabla, o bien 0 si no hay valores */
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
