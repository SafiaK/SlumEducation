package com.uog.umarkhan.slumeducation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserDetailActivity extends AppCompatActivity {

    Button ok;
    EditText Name,Phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        ok=(Button)findViewById(R.id.button2);
        Name=(EditText)findViewById(R.id.editTextName);
        Phone=(EditText)findViewById(R.id.editTextPhomeNumber);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=Name.getText().toString();
                String phone=Phone.getText().toString();
                if(name.isEmpty() || phone.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Details in both fields",Toast.LENGTH_SHORT).show();

                }
                else
                {


                    Intent intent=new Intent();
                    intent.putExtra("name",name);
                    intent.putExtra("phone",phone);
                    setResult(RESULT_OK,intent);

                    finish();

                }

            }
        });
    }
}
