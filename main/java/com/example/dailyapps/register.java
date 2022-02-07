package com.example.dailyapps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dailyapps.connection.ConnectionClass;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import kotlin.Suppress;

public class register extends AppCompatActivity {

    EditText name, email, password;
    Button registerbtn;
    TextView status;

    Connection con;
    Statement stmt;
    private Log log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        status = (TextView) findViewById(R.id.status);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new register.registeruser().execute("");
            }
        });
    }

    public class registeruser extends AsyncTask<String, String, String>{

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            status.setText("Sending data to database");
        }

        @Override
        protected void onPostExecute(String s) {
            status.setText("Register Successful");
            name.setText("");
            email.setText("");
            password.setText("");
        }

        @Override
        protected String doInBackground(String... strings){
            try {
                con = connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString());
                if(con == null){
                    z = "Check Internet Connection";
                }
                else {
                    String sql = "Insert Into register (name, email, password) values ('"+name.getText()+"','"+email.getText()+"','"+password.getText()+"')";
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql);
                }

            }catch (Exception e){
                isSuccess = false;
                z = e.getMessage();
            }

            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionClass (String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection= null;
        String connectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL= "jdbc:jtds:sqlserver://"+ server + "/" + database + ";user" + user + ";password" + password + ";";
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            log.e( "SQL Connection Error :", e.getMessage());
        }
        return connection;
    }
}