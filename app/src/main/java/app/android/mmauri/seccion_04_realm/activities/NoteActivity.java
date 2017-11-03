package app.android.mmauri.seccion_04_realm.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import app.android.mmauri.seccion_04_realm.R;
import app.android.mmauri.seccion_04_realm.adapters.NoteAdapter;
import app.android.mmauri.seccion_04_realm.models.Board;
import app.android.mmauri.seccion_04_realm.models.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board> {

    private ListView listView;
    private FloatingActionButton fab;

    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private Realm realm;

    private int boardId;
    private Board board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null)
            boardId = getIntent().getExtras().getInt("id");

        board = realm.where(Board.class).equalTo("id", boardId).findFirst();
        board.addChangeListener(this);
        notes = board.getNotes();

        /* Cambiamos el titulo del activity */
        this.setTitle(board.getTitle());

        fab = (FloatingActionButton) findViewById(R.id.fabAddNote);
        listView = (ListView) findViewById(R.id.listViewNote);
        adapter = new NoteAdapter(this, notes, R.layout.list_view_note_item);

        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingNote("Add a new note", "Type a note for " + board.getTitle() + ".");
            }
        });

    }


    //** Dialogs **//

    private void showAlertForCreatingNote(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewNote);

        /* Insertamos el boton "Add", y configuramos su comportamiento */
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String note = input.getText().toString().trim();
                if (note.length() > 0)
                    createNewNote(note);
                else
                    Toast.makeText(getApplicationContext(), "The note can't be empty", Toast.LENGTH_SHORT).show();
            }
        });

        /* Creamos el alert y lo mostramos */
        /* builder.create().show(); */
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertToEditNote(String title, String message, final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewNote);
        input.setText(note.getDescription());

        /* Insertamos el boton "Add", y configuramos su comportamiento */
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String noteName = input.getText().toString().trim();

                if (noteName.length() == 0)
                    Toast.makeText(getApplicationContext(), "The note cannot be empty to edit itself", Toast.LENGTH_SHORT).show();
                else if (noteName.equals(note.getDescription()))
                    Toast.makeText(getApplicationContext(), "The note is the same than it was before", Toast.LENGTH_SHORT).show();
                else
                    editNote(noteName, note);
            }
        });

        /* Creamos el alert y lo mostramos */
        /* builder.create().show(); */
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //** CRUD Actions **//

    private void createNewNote(String note) {
        realm.beginTransaction();
        Note _note = new Note(note);
        realm.copyToRealm(_note);       // Guarda la nota en la BD
        board.getNotes().add(_note);
        realm.commitTransaction();
    }

    private void editNote(String newNoteDescription, Note note) {
        realm.beginTransaction();
        note.setDescription(newNoteDescription);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    private void deleteNote(Note note) {
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }

    private void deleteAllNotes() {
        realm.beginTransaction();
        /* Borramos todas las notas referidas a la pizzarra */
        board.getNotes().deleteAllFromRealm();
        realm.commitTransaction();
    }


    //** Events **//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                deleteAllNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_note_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.delete_note:
                deleteNote(notes.get(info.position));
                return true;
            case R.id.edit_note:
                showAlertToEditNote("Edit note", "Change the note description", notes.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(Board element) {
        adapter.notifyDataSetChanged();
    }
}
