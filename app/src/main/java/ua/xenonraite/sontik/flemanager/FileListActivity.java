package ua.xenonraite.sontik.flemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.xenonraite.sontik.flemanager.data.FileList;
import ua.xenonraite.sontik.flemanager.http.FileManagerClient;
import ua.xenonraite.sontik.flemanager.view.FileListArrayAdapter;

import static android.widget.AdapterView.*;

public class FileListActivity extends AppCompatActivity {

    ListView fileListView;

    public static final int REFRESH_LIST = 0;
    public static final int FILE_DELETED = 1;
    public static final int RENAME_SUCSESS = 2;
    public static final int RENAME_FAILURE = 3;
    public static final int MSG = 4;
    public static final int MSG_DOWNLOADED = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fileListView = (ListView) findViewById(R.id.listView);
        fileListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        fileListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String fileName = FileList.getFileList().get(position).getFileName();
                AlertDialog.Builder builder = new AlertDialog.Builder(FileListActivity.this);
                builder.setTitle(getResources().getString(R.string.dialog_rename_title));

                final EditText input = new EditText(FileListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton(getResources().getString(R.string.dialog_rename_button_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newname = input.getText().toString();
                        FileManagerClient.renameFileJSON(FileListActivity.this, position, newname);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.dialog_rename_button_cencel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });

        FileManagerClient.getFileList(this);
        Log.d("FileListActivity", "getFileList");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_list, menu);
        return true;
    }

    public void showToastMessage(Activity activity,int codeMsg, String appendMsg) {
        if(activity == null)
            return;
        switch (codeMsg) {
            case RENAME_SUCSESS:
                Toast.makeText(activity, activity.getResources().getString(R.string.msg_file_renamed) + " " + ((appendMsg == null) ? "" : appendMsg), Toast.LENGTH_SHORT).show();
                break;
            case FILE_DELETED:
                Toast.makeText(activity, activity.getString(R.string.msg_files_deleted) + " " + ((appendMsg == null) ? "" : appendMsg), Toast.LENGTH_SHORT).show();
                break;
            case REFRESH_LIST:
                Toast.makeText(activity,activity.getResources().getString(R.string.msg_file_list_loaded) + " " + ((appendMsg == null) ? "" : appendMsg), Toast.LENGTH_SHORT).show();
                break;
            case RENAME_FAILURE:
                break;
            case MSG_DOWNLOADED:
                Toast.makeText(activity,activity.getResources().getString(R.string.msg_files_dowloaded) + " " + ((appendMsg == null) ? "" : appendMsg), Toast.LENGTH_SHORT).show();
                break;
            case MSG:
                Toast.makeText(activity, appendMsg == null ? "+" : appendMsg, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void refreshFileListView() {
        fileListView.setAdapter(new FileListArrayAdapter(FileListActivity.this, 0, FileList.getFileList()));
        Log.d("FileListActivity", "setAdapter");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                FileManagerClient.getFileList(this);
                Log.d("FileListActivity", "getFileList");
                return true;
            case R.id.action_delete:
                ArrayList<Integer> listIds = new ArrayList<Integer>();
                SparseBooleanArray checked = fileListView.getCheckedItemPositions();
                //print to log check items
                /*
                if (checked != null) {
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.valueAt(i)) {
                            String _item = fileListView.getAdapter().getItem(
                                    checked.keyAt(i)).toString();
                            Log.i("TEST", _item + " was selected");
                        }
                    }
                }*/
                for (int i = 0; i < fileListView.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        listIds.add(i);
                        Log.d("FileListActivity", "cheked " + i);
                    }
                }
                if(listIds.size() == 0)
                    return true;
                Log.d("FileListActivity", "action_delete");
                FileManagerClient.deleteFilesByListWithJson(FileListActivity.this, listIds);
                return true;
            case R.id.action_download:
                ArrayList<Integer> listIdsLoLoad = new ArrayList<Integer>();
                SparseBooleanArray caseFile = fileListView.getCheckedItemPositions();for (int i = 0; i < fileListView.getAdapter().getCount(); i++) {
                if (caseFile.get(i)) {
                    listIdsLoLoad.add(i);
                    Log.d("FileListActivity", "cheked " + i);
                }
            }
                Log.d("FileListActivity", "action_download");
                FileManagerClient.downloadFileTest(this, listIdsLoLoad);
                return true;
            default:
                showToastMessage(this,MSG, getResources().getString(R.string.error_notsupport));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
