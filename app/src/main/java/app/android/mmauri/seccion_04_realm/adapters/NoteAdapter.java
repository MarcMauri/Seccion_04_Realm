package app.android.mmauri.seccion_04_realm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import app.android.mmauri.seccion_04_realm.R;
import app.android.mmauri.seccion_04_realm.models.Note;

/**
 * Created by marc on 10/21/17.
 */

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> list;
    private int layout;

    public NoteAdapter(Context context, List<Note> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Note getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.description = (TextView) convertView.findViewById(R.id.textViewNoteDescription);
            vh.createdAt = (TextView) convertView.findViewById(R.id.textViewNoteCreatedAt);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        Note note = list.get(position);
        vh.description.setText(note.getDescription());

        /* Formateamos la fecha */
        DateFormat df = new SimpleDateFormat("dd/MM/yyy");
        String createdAt = df.format(note.getCreatedAt());
        vh.createdAt.setText(createdAt);

        return convertView;
    }

    public class ViewHolder {
        TextView description;
        TextView createdAt;
    }
}
