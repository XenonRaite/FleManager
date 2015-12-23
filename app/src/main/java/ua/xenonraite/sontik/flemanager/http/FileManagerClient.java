/**
 * Created by Sontik on 22.12.2015.
 */
package ua.xenonraite.sontik.flemanager.http;


import android.util.Log;

import com.loopj.android.http.*;


import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import ua.xenonraite.sontik.flemanager.FileListActivity;
import ua.xenonraite.sontik.flemanager.data.FileCell;
import ua.xenonraite.sontik.flemanager.data.FileList;


public class FileManagerClient {

    private static final String BASE_URL = "http://109.86.155.68:8050/FileManager/webresources/filemanager";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void getFileList(final FileListActivity activity) {
        client.get(getAbsoluteUrl(ServerAPI.GET_FILE_LIST), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                if (statusCode == HttpURLConnection.HTTP_OK) {
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //need refresh list
                    activity.refreshFileListView();
                    activity.showToastMessage(FileListActivity.REFRESH_LIST, null);
                }
            }
        });
    }


    @Deprecated
    public static void deleteFilesByList(final FileListActivity fileListActivity, ArrayList<Integer> ids) {
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
                fileListActivity.showToastMessage(FileListActivity.FILE_DELETED, null);
                //need refresh list
                FileManagerClient.getFileList(fileListActivity);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Log.d("FileListActivity", "deleteFiles onFailure");
            }
        });

    }
    public static void deleteFilesByListWithJson(final FileListActivity fileListActivity, ArrayList<Integer> ids) {

        JSONArray  jsonArray = new JSONArray();
        for(int id : ids){
            jsonArray.put(id);
        }


        JSONObject obj=new JSONObject();
        try {
            obj.put("ids",jsonArray);
        } catch (JSONException e) {
            Log.d("JSON ERROR","JSONException");
            e.printStackTrace();
        }

        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
            Log.d("JSON",obj.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.delete(fileListActivity, getAbsoluteUrl(ServerAPI.DELITE_FILES_NEW_METHOD), entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                Log.d("FileListActivity", "deleteFiles new method onSuccess");
                fileListActivity.showToastMessage(FileListActivity.FILE_DELETED, null);
                //need refresh list
                FileManagerClient.getFileList(fileListActivity);

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Log.d("FileListActivity", "deleteFiles new method onFailure");

            }
        });

    }
    public static void renameFile(final FileListActivity fileListActivity, int indexFile, String fileNewName) {
        String oldnamefile = FileList.getFileList().get(indexFile).getFileName();

        Log.d("FileManagerClient",getAbsoluteUrl(ServerAPI.RENAME_FILE) + "/" + oldnamefile + "/" + fileNewName);
        client.put(getAbsoluteUrl(ServerAPI.RENAME_FILE) + "/" + oldnamefile + "/" + fileNewName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {


                fileListActivity.showToastMessage(FileListActivity.RENAME_SUCSESS, null);
                //need refresh list
                FileManagerClient.getFileList(fileListActivity);

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                fileListActivity.showToastMessage(FileListActivity.RENAME_FAILURE, null);
            }
        });

    }

}
