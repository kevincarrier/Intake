package org.intakers.intake;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by Kevin on 2/26/17.
 */

public class FoodMappingActivity extends Activity {

    private MobileServiceClient mClient;
    private MobileServiceTable<FoodMapping> mFoodMappingTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmapping);

//        Intent receiveInfo = getIntent();
//        final String receiveFood = receiveInfo.getStringExtra("food");

        try{
            mClient = new MobileServiceClient(
                    "https://intakemobileapp.azurewebsites.net",
                    this).withFilter(new ProgressFilter());
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });
            mFoodMappingTable = mClient.getTable(FoodMapping.class);
            initLocalStore().get();
            //addItem();
            Log.d("This getData()", "asd");
            getData();

        }catch (MalformedURLException e) {
            Log.d("Malformed Exception", "There was an error creating the Mobile Service. Verify the URL. Error: " + e);
        } catch (Exception e){
            Log.d("General Exception", "Error: " + e);
        }

        //getData();

    }

    private void getData() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    Log.d("before final", "does");
                    final List<FoodMapping> results = refreshItemsFromMobileServiceTable();

                    Log.d("After final result", "plz");
                    //Offline Sync
                    //final List<ToDoItem> results = refreshItemsFromMobileServiceTableSyncTable();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mAdapter.clear();

                            for (FoodMapping item : results) {
                                Log.d("Add New Food", "Add: " + item.getFood());
                                Log.d("Add New URL", "Add: " + item.getUrl());

                                Intent sendURL = new Intent(FoodMappingActivity.this, ResultActivity.class);
                                sendURL.putExtra("URL", item.getUrl());
                                Log.d("URL", item.getUrl());
                                startActivity(sendURL);



                                //mAdapter.add(item);
                            }
                        }
                    });
                } catch (final Exception e){
                    Log.d("General Exception", "Error: " + e);
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    private List<FoodMapping> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {

        Intent receiveInfo = getIntent();
        final String receiveFood = receiveInfo.getStringExtra("food");
        Log.d("hi" , receiveFood);

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(receiveFood);

        //Error is here
        return mFoodMappingTable.where().field("food").
                eq(val(receiveFood)).execute().get();
    }

    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("food", ColumnDataType.String);
                    tableDefinition.put("url", ColumnDataType.String);

                    localStore.defineTable("FoodMapping", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    Log.d("General Exception", "Error: " + e);
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }

    public void addItem() {
        if (mClient == null) {
            return;
        }

        // Create a new item
        final FoodMapping item = new FoodMapping();

        item.setFood("Vanilla Ice Cream");
        item.setUrl("https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=9mm0lhA9e5I5iXMmxvVX9AVQAydZunC62oFvbjMS&nutrients=208&nutrients=203&nutrients=255&nutrients=204&nutrients=205&nutrients=291&nutrients=269&nutrients=301&nutrients=303&nutrients=304&nutrients=305&nutrients=306&nutrients=307&nutrients=309&nutrients=401&nutrients=324&nutrients=605&nutrients=606&nutrients=262&nutrients=323&ndbno=19088");

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final FoodMapping entity = addItemInTable(item);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*if(!entity.isComplete()){
                                mAdapter.add(entity);
                            }*/
                        }
                    });
                } catch (final Exception e) {
                    Log.d("General Exception", "Error: " + e);
                }
                return null;
            }
        };

        runAsyncTask(task);

        //mTextNewToDo.setText("");
    }

    /**
     * Add an item to the Mobile Service Table
     *
     * @param item
     *            The item to Add
     */
    public FoodMapping addItemInTable(FoodMapping item) throws ExecutionException, InterruptedException {
        FoodMapping entity = mFoodMappingTable.insert(item).get();
        return entity;
    }
}
