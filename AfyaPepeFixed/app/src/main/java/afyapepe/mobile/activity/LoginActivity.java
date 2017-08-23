package afyapepe.mobile.activity;

import afyapepe.mobile.R;
import afyapepe.mobile.app.AppConfig;
import afyapepe.mobile.app.AppController;
import afyapepe.mobile.helper.SQLiteHandler;
import afyapepe.mobile.helper.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLinkToRegister;
    private Button btnLogin;
    private SQLiteHandler db;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.inputEmail = (EditText) findViewById(R.id.email);
        this.inputPassword = (EditText) findViewById(R.id.password);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        this.pDialog = new ProgressDialog(this);
        this.pDialog.setCancelable(false);
        this.db = new SQLiteHandler(getApplicationContext());
        this.session = new SessionManager(getApplicationContext());
        if (this.session.isLoggedIn()) {
            startActivity(new Intent(this, registrarpage.class));
            finish();
        }
        this.btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String email = LoginActivity.this.inputEmail.getText().toString().trim();
                String password = LoginActivity.this.inputPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Please enter the credentials!", 1).show();
                } else {
                    LoginActivity.this.checkLogin(email, password);
                }
            }
        });
        this.btnLinkToRegister.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this.getApplicationContext(), Registactiv.class));
                LoginActivity.this.finish();
            }
        });
    }

    private void checkLogin(String email, String password) {
        this.pDialog.setMessage("️ Logging in ️");
        showDialog();
        final String str = email;
        final String str2 = password;
        StringRequest strReq = new StringRequest(1, AppConfig.URL_LOGIN, new Listener<String>() {
            public void onResponse(String response) {
                Log.d(LoginActivity.TAG, "Login Response: " + response.toString());
                LoginActivity.this.hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("error")) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), jObj.getString("error_msg"), 1).show();
                        return;
                    }
                    LoginActivity.this.session.setLogin(true);
                    String uid = jObj.getString("uid");
                    String foop = jObj.getString("roles");
                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString(ParseJSON.KEY_NAME);
                    String email = user.getString("email");
                    String created_at = user.getString("created_at");
                    String jippy = foop.toString();
                    if (jippy.equals("Registrar")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, registrarpage.class));
                    } else if (jippy.equals("Admin")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, Admin.class));
                    } else if (jippy.equals("Nurse")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, Nurse.class));
                    } else if (jippy.equals("Doctor")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, Doctorn.class));
                    } else if (jippy.equals("Manufacturer")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, Manufacturer.class));
                    } else if (jippy.equals("Patient")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, Patient.class));
                    } else if (jippy.equals("Admin")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, Admin.class));
                    } else if (jippy.equals("Private")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, Private.class));
                    } else if (jippy.equals("FacilityAdmin")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, FacilityAdm.class));
                    } else if (jippy.equals("Pharmacy_store_keeper")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, PharmSto.class));
                    } else if (jippy.equals("Pharmacy_manager")) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, PharmMan.class));
                    }
                    LoginActivity.this.db.addUser(name, email, uid, created_at);
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Json error: " + e.getMessage(), 1).show();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.e(LoginActivity.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(LoginActivity.this.getApplicationContext(), error.getMessage(), 1).show();
                LoginActivity.this.hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("email", str);
                params.put("password", str2);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "req_login");
    }

    private void showDialog() {
        if (!this.pDialog.isShowing()) {
            this.pDialog.show();
        }
    }

    private void hideDialog() {
        if (this.pDialog.isShowing()) {
            this.pDialog.dismiss();
        }
    }
}