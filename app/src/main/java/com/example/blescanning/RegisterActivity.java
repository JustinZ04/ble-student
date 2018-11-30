package com.example.blescanning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity
{
    private String nid;
    private String email;
    private String uuid;
    private String name;
    private Button register;
    private RequestQueue requests;
    private EditText nidform;
    private EditText emailform;
    private EditText nameform;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nidform = findViewById(R.id.Nid);
        emailform = findViewById(R.id.Email);
        nameform = findViewById(R.id.Name);
        register = findViewById(R.id.register);

        requests = SingletonAPICalls.getInstance(this).getRequestQueue();

        if(!(SaveSharedPreference.getUUID(RegisterActivity.this).length() == 0)){
            Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        register();
                    }
                }).start();

            }
        });
    }

    private void easyToast(String string){
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(RegisterActivity.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }

    private void register(){
        String url = Constants.URL + Constants.registerStudent;

        uuid = UUID.randomUUID().toString();
        email = emailform.getText().toString();
        nid = nidform.getText().toString();
        name = nameform.getText().toString();

        if(email.matches("")){
            easyToast("Please put in an email!");
            return;
        }

        if(name.matches("")){
            easyToast("Please put in a Password!");
            return;
        }

        if(nid.matches("")){
            easyToast("Please put in a Password!");
            return;
        }

        easyToast("Cool");

        JSONObject obj = new JSONObject();

        try
        {
            obj.put("name", name);
            obj.put("nid", nid);
            obj.put("email", email);
            obj.put("uuid", uuid);
        }
        catch (JSONException e)
        {
            easyToast("Error with json");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {

                            if (response.has("error")) {
                                easyToast("User not found");
                            } else {
                                easyToast("Verified!");
                                Constants.LOGGED_IN = true;
                                SaveSharedPreference.setStuNID(RegisterActivity.this, nid);
                                SaveSharedPreference.setStuUUID(RegisterActivity.this, uuid);

                                Intent loginIntent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                startActivity(loginIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                System.out.println(error.getCause().toString());
                easyToast("Volley Error!");
            }
        });

        requests.add(request);
    }


    @Override
    protected void onDestroy()
    {
//        stopAdvertising();
        super.onDestroy();
    }
}
