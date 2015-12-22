package ua.xenonraite.sontik.flemanager.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.*;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ua.xenonraite.sontik.flemanager.R;
import ua.xenonraite.sontik.flemanager.data.FileCell;
import ua.xenonraite.sontik.flemanager.data.FileList;


/**
 * Created by Sontik on 22.12.2015.
 */
public class FileManagerClient {

    private static final String BASE_URL = "http://109.86.155.68:8050/FileManager/webresources/filemanager";

    private static AsyncHttpClient client = new AsyncHttpClient();


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void getFileList(final Handler h) {
        client.get(getAbsoluteUrl(ServerAPI.GET_FILE_LIST), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                FileList.cleanFileList();
                try {
                    Log.d("FileManagerClient", response.getJSONArray("fileList").getString(0));
                    //load list file from JSON

                    int listSize = response.getJSONArray("fileList").length();
                    for (int i = 0; i < listSize; i++) {
                        FileList.addFileList(new FileCell(response.getJSONArray("fileList").getString(i),
                                response.getJSONArray("fileSizeList").getInt(i)));
                    }
                    for (FileCell cell : FileList.getFileList()) {
                        Log.d("FileManagerClient", "Name:" + cell.getFileName() + " file size :" + cell.getFileSize());
                    }
                    h.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void deleteFilesByList(final Handler h, ArrayList<Integer> ids) {
        Log.d("FileListActivity", "deleteFilesByList");

        RequestParams requestParams =new RequestParams();
        requestParams.add("tst","test");


        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (int i : ids) {
            if (first) {
                stringBuilder.append(FileList.getFileList().get(i).getFileName());
                first = false;
            } else
                stringBuilder.append(";" + FileList.getFileList().get(i).getFileName());

        }

        Log.d("FileManagerClient","deleted:"+stringBuilder.toString());


        client.delete(getAbsoluteUrl(ServerAPI.DELITE_FILES) + "/files;" + stringBuilder.toString(),new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                Log.d("FileListActivity", "deleteFiles onSuccess");
                h.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Log.d("FileListActivity", "deleteFiles onSuccess");
            }
        });

    }

    public static void renameFile(final Handler h, int indexFile, String fileNewName) {
        String oldnamefile = FileList.getFileList().get(indexFile).getFileName();

        Log.d("FileManagerClient",getAbsoluteUrl(ServerAPI.RENAME_FILE) + "/" + oldnamefile + "/" + fileNewName);
        client.put(getAbsoluteUrl(ServerAPI.RENAME_FILE) + "/" + oldnamefile + "/" + fileNewName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                h.sendEmptyMessage(3);

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                h.sendEmptyMessage(4);
            }
        });

    }

}
