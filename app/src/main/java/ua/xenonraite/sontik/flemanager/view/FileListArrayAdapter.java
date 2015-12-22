package ua.xenonraite.sontik.flemanager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.xenonraite.sontik.flemanager.R;
import ua.xenonraite.sontik.flemanager.data.FileCell;
import ua.xenonraite.sontik.flemanager.data.FileList;

/**
 * Created by Sontik on 22.12.2015.
 */
public class FileListArrayAdapter extends ArrayAdapter<FileCell> {

    private final Context context;
    private final ArrayList<FileCell> value;

    public FileListArrayAdapter(Context context, int resource, ArrayList<FileCell> objects) {
        super(context, R.layout.item_list, objects);
        this.context = context;
        this.value = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list, parent, false);
        //TextView fileNameView = (TextView) rowView.findViewById(R.id.filename);
        //TextView FileSizeView = (TextView) rowView.findViewById(R.id.filesize);
        CheckedTextView checkedTextView = (CheckedTextView)rowView.findViewById(R.id.textCheck);
        checkedTextView.setText(FileList.getFileList().get(position).getFileName()+"  size:"+FileList.getFileList().get(position).getFileSize()+"byte");



        //listView.setItemChecked(position,true);
        return rowView;
    }


}
