package afyapepe.mobile.activity;



import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import afyapepe.mobile.R;
import afyapepe.mobile.helper.SQLiteHandler;
import afyapepe.mobile.helper.SessionManager;

public class registrarpage extends AppCompatActivity {
    private TextView tvData;
    BufferedReader reader;
    private SQLiteHandler db;
    private SessionManager session;
    private TextView txtName;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarpage);
        Button btnHit=(Button)findViewById(R.id.btnHit);

        Button btnout=(Button)findViewById(R.id.btnLogout);


        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);


        tvData=(TextView) findViewById(R.id.tvJsonitem);

        db = new SQLiteHandler(getApplicationContext());


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);









        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new JSONtask().execute("http://afyapepe.com/afyapepe/users.txt");
            }
        });



      btnout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
           logoutUser();
          }
      });



    }
























    public class JSONtask extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... params) {











            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);

                }


                //this is where the real stuff goes on lol...
                String finalJson = buffer.toString();
                //The JSON starts with a curly bracket so we get the json object first
                //But I have to loop through because there are many objects in the list


                JSONObject parentJSON = new JSONObject(finalJson);
                //Then the square brackets, where we now access it using jsonarray


                JSONArray parentArray = parentJSON.getJSONArray("patients");

                StringBuffer finalBufferedString = new StringBuffer();
                for (int i = 0; i < parentArray.length(); i++) {
                    //In the jsonarray there are objects so we once again access them as I said above
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    String firstname = finalObject.getString("firstname");


                    String secondName = finalObject.getString("secondName");
                    int age= finalObject.getInt("age");
                    finalBufferedString.append(firstname + " " +secondName  + "\n"+age);

                }
                return finalBufferedString.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null) {
                    connection.disconnect();


                }

                try {
                    if(reader!=null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {





            super.onPostExecute(result);
            tvData.setText(result);
        }
    }








    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(registrarpage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}










