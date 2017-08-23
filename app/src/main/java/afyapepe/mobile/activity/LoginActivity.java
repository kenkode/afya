
package afyapepe.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import afyapepe.mobile.R;
import afyapepe.mobile.app.AppConfig;
import afyapepe.mobile.app.AppController;
import afyapepe.mobile.helper.SQLiteHandler;
import afyapepe.mobile.helper.SessionManager;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
       if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, registrarpage.class);
            startActivity(intent);
            finish();
        }




        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Registactiv.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("️ Logging in ️");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        String foop = jObj.getString("roles");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        String jippy=foop.toString();
                        if(jippy.equals("Registrar"))

                        {

                           Intent regist=new Intent(LoginActivity.this,registrarpage.class);
                            startActivity(regist);


                        }



                        else
                        if(jippy.equals("Admin"))

                        {

                            Intent regist1=new Intent(LoginActivity.this,Admin.class);
                            startActivity(regist1);


                        }


                        else
                        if(jippy.equals("Nurse"))

                        {

                            Intent regist2=new Intent(LoginActivity.this,PatientsActivity.class);
                            startActivity(regist2);


                        }



                        else
                        if(jippy.equals("Doctor"))

                        {

                            Intent regist3=new Intent(LoginActivity.this,Doctorn.class);
                            startActivity(regist3);


                        }


                        else
                        if(jippy.equals("Manufacturer"))

                        {

                            Intent regist4=new Intent(LoginActivity.this,Manufacturer.class);
                            startActivity(regist4);


                        }



                        else
                        if(jippy.equals("Patient"))

                        {

                            Intent regist5=new Intent(LoginActivity.this,Patient.class);
                            startActivity(regist5);


                        }



                        else
                        if(jippy.equals("Admin"))

                        {

                            Intent regist6=new Intent(LoginActivity.this,Admin.class);
                            startActivity(regist6);


                        }



                        else
                        if(jippy.equals("Private"))

                        {

                            Intent regist7=new Intent(LoginActivity.this,Private.class);
                            startActivity(regist7);


                        }





                        else
                        if(jippy.equals("FacilityAdmin"))

                        {

                            Intent regist8=new Intent(LoginActivity.this,FacilityAdm.class);
                            startActivity(regist8);


                        }


                        else
                        if(jippy.equals("Pharmacy_store_keeper"))

                        {

                            Intent regist9=new Intent(LoginActivity.this,PharmSto.class);
                            startActivity(regist9);


                        }


                        else
                        if(jippy.equals("Pharmacy_manager"))

                        {

                            Intent regist10=new Intent(LoginActivity.this,PharmMan.class);
                            startActivity(regist10);


                        }



                        else
                        if(jippy.equals("Patient"))

                        {

                            Intent regist11=new Intent(LoginActivity.this,indpatients.class);
                            startActivity(regist11);


                        }




                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        // Launch main activity
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
