package marchenko.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class RegActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mPass;
    private EditText mPass2;
    int k = 0;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mName = (EditText) findViewById(R.id.editTextUsername);
        mPass = (EditText) findViewById(R.id.editTextPass);
        mPass2 = (EditText) findViewById(R.id.editTextPass2);
        Button mReg = (Button) findViewById((R.id.buttonReg));
        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration();
            }
        });
    }

    private void Registration()
    {
        String name = mName.getText().toString();
        String pass = mPass.getText().toString();
        String pass2 = mPass2.getText().toString();

        if (pass.equals(pass2) && pass.trim().length() != 0 && name.trim().length() != 0) {
            k = 0;
            new ConnectThread(name, pass);
            while(k == 0)
            {
                //System.out.println("waiting for results");
            }
            Intent intent = new Intent(RegActivity.this, LogActivity.class);
            startActivity(intent);
        }
        else if (name.trim().length() == 0 || pass.trim().length() == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Ошибка");
            alert.setMessage("Пожалуйста, заполните пустое поле");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        }
        else if (!pass.equals(pass2)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Ошибка");
            alert.setMessage("Пароли не совпадают, попробуйте еще раз");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        }
    }

    class ConnectThread implements Runnable
    {
        Thread thread;
        String name;
        String password;

        ConnectThread(String _name, String _password)
        {
            name = _name;
            password = _password;
            thread = new Thread(this, "connectToWeb");
            System.out.println("Create thread");
            thread.start();
        }

        @Override
        public void run() {
            SetMessage(name, password);
            k = 1;
        }
    }

    public void SetMessage(String _name, String _pass)
    {
        DefaultHttpClient hc = new DefaultHttpClient();
        ResponseHandler<String> res = new BasicResponseHandler();
        HttpPost http = new HttpPost("http://azbuss.com/ruslan/loginMap.php?action=save&name="+(_name)+"&password="+(_pass));
        //String response="";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(_name, "name"));
            nameValuePairs.add(new BasicNameValuePair(_pass, "pass"));
            http.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = hc.execute(http);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
