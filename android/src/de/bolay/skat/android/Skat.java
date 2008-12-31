package de.bolay.skat.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Skat extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    Button button = (Button)findViewById(R.id.login_button);
    button.setOnClickListener(loginListener);
  }

  private OnClickListener loginListener = new OnClickListener() {
    public void onClick(View v) {
      String username = ((EditText) findViewById(R.id.username_field))
          .getText().toString();
      String password = ((EditText) findViewById(R.id.password_field))
          .getText().toString();
      Intent loginData = new Intent(Skat.this, ServerConnection.class)
          .setAction(ServerConnection.LOGIN)
          .putExtra("username", username)
          .putExtra("password", password);
      startService(loginData);
      //setResult(RESULT_OK, loginData);
      //finish();
    }
  };
}