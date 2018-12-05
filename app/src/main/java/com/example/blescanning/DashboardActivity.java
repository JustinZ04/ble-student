package com.example.blescanning;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DashboardActivity extends AppCompatActivity
{
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FragmentManager fm = getSupportFragmentManager();
    private RecyclerView recyclerView;
    private LectureAdapter adapter;
    private List<Lecture> lectureList;
    private RequestQueue queue;
    private List<String> lectureId = new ArrayList<>();
    private List<JSONArray> lectureInfo = new ArrayList();
    private boolean lectureListFilled = false;
    private Button addClass;
    private EditText regcode;
    private String regcodestring;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lectureList = new ArrayList<>();
        queue = SingletonAPICalls.getInstance(this).getRequestQueue();
        addClass = findViewById(R.id.addClass);
        regcode = findViewById(R.id.regCode);

        addClass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addclass();
                    }
                }).start();

            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadLectures();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS,
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       // getMenuInflater().inflate(R.menu.menu_items, menu);

        return true;
    }

    /*public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        { private void easyToast(String string){
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(RegisterActivity.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }
            case R.id.
        }
    }*/

    private void easyToast(String string){
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(DashboardActivity.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }

    public void addclass(){
        regcodestring = regcode.getText().toString();
        String url = Constants.URL + Constants.ADD_TO_CLASS;
        JSONObject request = new JSONObject();
        try{
            request.put("nid", SaveSharedPreference.getStuNID(DashboardActivity.this));
            request.put("reg_code", regcodestring);
        }

        catch(JSONException e)
        {
            easyToast("Error try again");
        }

        JsonObjectRequest classRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lectureId.clear();
                            lectureInfo.clear();
                            lectureList.clear();
                            recyclerView = findViewById(R.id.recycler_view);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                            loadLectures();

                        } catch (Exception e) {
                            e.printStackTrace();
                            easyToast("Error!");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                easyToast("Error!");
            }
        }

        );
        queue.add(classRequest);
    }

    public void loadLectures()
    {
        String getClassIdurl = Constants.URL + Constants.GET_LECTURE_ID + SaveSharedPreference.getStuNID(DashboardActivity.this);

        StringRequest request = new StringRequest(Request.Method.GET, getClassIdurl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONArray lectures = new JSONArray(response);
                    JSONObject o;

                    for(int i = 0; i < lectures.length(); i++)
                    {
                        //Toast.makeText(getApplicationContext(), lectures.get(i).toString(), Toast.LENGTH_LONG);
                        o = lectures.getJSONObject(i);
                        lectureId.add(o.getString("class_id"));
                        Log.v("id", lectureId.get(i));
                    }

                    getLectureInfo();
                   /* if(lectureListFilled)
                    {
                        Log.v("list", lectureInfo.get(0).toString() + "\n" + lectureInfo.get(1).toString() + "\n" + lectureInfo.get(2).toString());
                    }*/
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            /*@Override
            public void onResponse(String response)
            {
                try {
                    JSONArray lectures = new JSONArray(response);

                    for (int i = 0; i < lectures.length(); i++) {
                        JSONObject lectureObject = lectures.getJSONObject(i);

                        Lecture newLecture = new Lecture(
                                lectureObject.getString("_id"),
                                lectureObject.getString("courseID"),
                                lectureObject.getString("className"),
                                lectureObject.getString("startTime"),
                                lectureObject.getString("createdByProfNID"),
                                lectureObject.getString("endTime"));

                        lectureList.add(newLecture);


                    }

                    adapter = new LectureAdapter(DashboardActivity.this, lectureList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(DashboardActivity.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(adapter);

                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener()
                    {
                        @Override
                        public void onClick(View view, int position)
                        {
                            Lecture currentLecture = lectureList.get(position);
                            Toast.makeText(getApplicationContext(), currentLecture.getName(), Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onLongClick(View view, int position)
                        {

                        }
                    }));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        queue.add(request);
    }

    private void getLectureInfo()
    {
        String url;
        adapter = new LectureAdapter(DashboardActivity.this, lectureList);

        for(int i = 0; i < lectureId.size(); i++)
        {
            url = Constants.URL + Constants.GET_LECTURE_INFO + lectureId.get(i);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONArray lecture = new JSONArray(response);
                        lectureInfo.add(lecture);
                        Log.v("info", "lecture " + lecture.getJSONObject(0).get("_id"));
                        Log.v("info", lectureInfo.toString());
                        lectureListFilled = true;

                        //for (int i = 0; i < lectureInfo.size(); i++) {
                            JSONArray lectureObject = lectureInfo.get(lectureInfo.size()-1);

                            Lecture newLecture = new Lecture(
                                    lectureObject.getJSONObject(0).get("_id").toString(),
                                    lectureObject.getJSONObject(0).get("courseID").toString(),
                                    lectureObject.getJSONObject(0).get("className").toString(),
                                    lectureObject.getJSONObject(0).get("startTime").toString(),
                                    lectureObject.getJSONObject(0).get("endTime").toString(),
                                    lectureObject.getJSONObject(0).get("createdByProfNID").toString());

                            lectureList.add(newLecture);


                        //}

                        recyclerView.addItemDecoration(new DividerItemDecoration(DashboardActivity.this, LinearLayoutManager.VERTICAL));
                        adapter.notifyDataSetChanged();
                        recyclerView.swapAdapter(adapter, true);

                        /*recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener()
                        {
                            @Override
                            public void onClick(View view, int position)
                            {
                                Lecture currentLecture = lectureList.get(position);
                                easyToast(currentLecture.getName());
                                Intent intent = new Intent(DashboardActivity.this, ScanActivity.class);
                                intent.putExtra("classID", currentLecture.getCourse_id());
                                intent.putExtra("className", currentLecture.getName());
                                startActivity(intent);
                            }

                            @Override
                            public void onLongClick(View view, int position)
                            {

                            }
                        }));*/
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            });

            queue.add(request);
        }

        // Moved out of the for loop
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Lecture currentLecture = lectureList.get(position);
                easyToast(currentLecture.getName());
                Intent intent = new Intent(DashboardActivity.this, ScanActivity.class);
                intent.putExtra("classID", currentLecture.getCourse_id());
                intent.putExtra("className", currentLecture.getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));
    }

    public void attend(View view)
    {
        Intent attendIntent = new Intent(this, AttendActivity.class);
        startActivity(attendIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch(requestCode)
        {
            case REQUEST_LOCATION_PERMISSION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // dont need to do anything
                }
                else
                {
                    AlertDialogFragment a = new AlertDialogFragment();
                    a.show(fm, "permissions");
                }
            }
        }
    }

}
