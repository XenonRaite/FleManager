package ua.xenonraite.sontik.flemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.xenonraite.sontik.flemanager.data.FileList;
import ua.xenonraite.sontik.flemanager.http.FileManagerClient;
import ua.xenonraite.sontik.flemanager.view.FileListArrayAdapter;

public class FileListActivity extends AppCompatActivity {

    ListView listView;

    final int REFRESH_LIST = 0;
    final int FILE_DELETED = 1;
    final int RENAME_SUCSESS = 3;
    final int RENAME_FAILURE = 4;

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        listView = (ListView) findViewById(R.id.listView);
//
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox);
//
//                boolean changeCheckOn = !checkBox.isChecked();
//                checkBox.setChecked(changeCheckOn);
//                listView.setItemChecked(position,changeCheckOn);
//            }
//        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String fileName = FileList.getFileList().get(position).getFileName();

                AlertDialog.Builder builder = new AlertDialog.Builder(FileListActivity.this);
                builder.setTitle("File rename");


                final EditText input = new EditText(FileListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);


                builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newname = input.getText().toString();
                        FileManagerClient.renameFile(h,position,newname);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


                return true;
            }
        });

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case REFRESH_LIST:

                       listView.setAdapter(new FileListArrayAdapter(FileListActivity.this, 0, FileList.getFileList()));
                        Log.d("FileListActivity", "setAdapter");
                        break;
                    case FILE_DELETED:
                        Toast.makeText(FileListActivity.this,"File Deleted",Toast.LENGTH_LONG).show();
                        break;
                    case RENAME_SUCSESS:
                        Toast.makeText(FileListActivity.this,"File renamed",Toast.LENGTH_LONG).show();
                        break;
                }
            };
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id== R.id.action_refresh){
            FileManagerClient.getFileList(h);
            Log.d("FileListActivity", "getFileList");
        }

        if (id == R.id.action_delete) {

            ArrayList<Integer> listIds = new ArrayList<Integer>();

            SparseBooleanArray checked = listView.getCheckedItemPositions();
            if (checked != null) {
                for (int i=0; i<checked.size(); i++) {
                    if (checked.valueAt(i)) {
                        String _item = listView.getAdapter().getItem(
                                checked.keyAt(i)).toString();
                        Log.i("TEST",_item + " was selected");
                    }
                }
            }

            for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                if (checked.get(i)) {
                    listIds.add(i);

                    Log.d("FileListActivity", "cheked "+i);
                }
            }

            Log.d("FileListActivity", "action_delete");
            FileManagerClient.deleteFilesByList(h,listIds);
        }

        return super.onOptionsItemSelected(item);
    }
}
