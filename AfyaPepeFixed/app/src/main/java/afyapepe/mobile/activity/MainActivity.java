package afyapepe.mobile.activity;

import afyapepe.mobile.R;
import afyapepe.mobile.helper.SQLiteHandler;
import afyapepe.mobile.helper.SessionManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;

public class MainActivity extends Activity {
	private Button btnLogout;
	private SQLiteHandler db;
	private SessionManager session;
	private TextView txtEmail;
	private TextView txtName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.txtName = (TextView) findViewById(R.id.name);
		this.txtEmail = (TextView) findViewById(R.id.email);
		this.btnLogout = (Button) findViewById(R.id.btnLogout);
		this.db = new SQLiteHandler(getApplicationContext());
		this.session = new SessionManager(getApplicationContext());
		if (!this.session.isLoggedIn()) {
			logoutUser();
		}
		HashMap<String, String> user = this.db.getUserDetails();
		String email = (String) user.get("email");
		this.txtName.setText((String) user.get(ParseJSON.KEY_NAME));
		this.txtEmail.setText(email);
		this.btnLogout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MainActivity.this.logoutUser();
			}
		});
	}

	private void logoutUser() {
		this.session.setLogin(false);
		this.db.deleteUsers();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
}