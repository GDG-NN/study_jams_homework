package marchenko.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class LogActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private String text;
    private int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mEmailView = (EditText) findViewById(R.id.EmailField);
        mPasswordView = (EditText) findViewById(R.id.PasswordField);

        Button mSignInButton = (Button) findViewById(R.id.SignInButton);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        Button mRegistration = (Button) findViewById(R.id.Registration);
        mRegistration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LogActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }
    private void Login()
    {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        k = 0;
        new ConnectThread(email, password);
        while(k==0) {
            //System.out.println("waiting for results");
        }
        if(text.equals("false")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Ошибка");
            alert.setMessage("Аккаунт с таким именем не существует");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        }
        if(text.equals("true")) {
            if(email.trim().length() == 0|| password.trim().length() == 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Ошибка");
                alert.setMessage("Пожалуйста, заполните пустое поле");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
            else {
            Intent intent = new Intent(LogActivity.this, MainActivity.class);
            startActivity(intent);
            }
        }
    }

    class ConnectThread implements Runnable
    {
        Thread thread;
        String email;
        String password;

        ConnectThread(String _email, String _password) {
            email = _email;
            password = _password;
            thread = new Thread(this, "connectToWeb");
            System.out.println("Create thread");
            thread.start();
        }

        @Override
        public void run() {
            text = GetMessage("http://azbuss.com/ruslan/loginMap.php?action=check&name=" + email + "&password=" + password);
            k = 1;
        }
        public String GetMessage(String urlsite) {
            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler<String> res = new BasicResponseHandler();
            HttpGet http = new HttpGet(urlsite);
            String response="";
            try {
                response = hc.execute(http, res);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return response;
        }
    }
}
