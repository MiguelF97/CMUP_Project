package pt.ua.smartelevator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pt.ua.smartelevator.data.AppViewModel;
import pt.ua.smartelevator.data.User;


public class MainActivity extends AppCompatActivity {

    EditText add_email, add_pass, add_building_pass;
    CheckBox rememberMe;
    Button login, register;
    Spinner building, floor;
    private AppViewModel viewModel;
    private static final String[] building_passwords = new String[]{"6000", "3810"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_email = findViewById(R.id.email);
        add_pass = findViewById(R.id.pass);
        add_building_pass = findViewById(R.id.buildingPass);
        rememberMe = findViewById(R.id.rememberMe);
        login = findViewById(R.id.login);
        register = findViewById(R.id.signup);
        building = findViewById(R.id.building);
        floor = findViewById(R.id.floor);

        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        List<Integer> floors_building1 = IntStream.range(1, 7).boxed().collect(Collectors.toList());
        List<Integer> floors_building2 = IntStream.range(1, 10).boxed().collect(Collectors.toList());


        building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String building_chosen= String.valueOf(building.getSelectedItem());

                if (building_chosen.equals("Building1")){
                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item,
                            floors_building1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter.notifyDataSetChanged();
                    floor.setAdapter(adapter);

                }

                else if (building_chosen.equals("Building2")){

                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item,
                            floors_building2);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter.notifyDataSetChanged();
                    floor.setAdapter(adapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result_code = loginUser();
                if (result_code == RESULT_OK) {
                    Intent i = new Intent(MainActivity.this, Home_Page.class);
                    User u = viewModel.getUser(add_email.getText().toString());
                    i.putExtra("Building", u.building);
                    i.putExtra("Floor", u.floor);
                    startActivity(i);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result_code = registerUser();
                if (result_code == RESULT_OK) {
                    Intent i = new Intent(MainActivity.this, Home_Page.class);
                    User u = viewModel.getUser(add_email.getText().toString());
                    i.putExtra("Building", u.building);
                    i.putExtra("Floor", u.floor);
                    startActivity(i);
                }
            }
        });

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){

                    SharedPreferences sp = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(MainActivity.this,"Checked", Toast.LENGTH_SHORT).show();
                }

                else if (!buttonView.isChecked()){

                    SharedPreferences sp = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(MainActivity.this,"Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int registerUser(){

        String email = add_email.getText().toString();
        String pass = add_pass.getText().toString();
        String building_pass = add_building_pass.getText().toString();
        String building_selected = String.valueOf(building.getSelectedItem());
        //int floor_selected = Integer.parseInt(floor.getSelectedItem().toString());
        int floor_selected = 5;
        int result_code = RESULT_CANCELED;


        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill out all the fields", Toast.LENGTH_LONG).show();
        }
        else if (!isValidPassword(pass) && pass.length() < 8){
            Toast.makeText(getApplicationContext(),
                    "Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character",
                    Toast.LENGTH_LONG).show();
        }
        else if (building.getSelectedItemPosition() < 0 || floor.getSelectedItemPosition() < 0){
            Toast.makeText(getApplicationContext(), "Please select a building and a floor", Toast.LENGTH_LONG).show();
        }
        else if (!building_pass.equals(building_passwords[building.getSelectedItemPosition()])){

            Toast.makeText(getApplicationContext(), "Incorrect building password", Toast.LENGTH_LONG).show();
        }
        else if(!viewModel.checkEmailAvailable(email)){
            Toast.makeText(getApplicationContext(), "Email alredy being used", Toast.LENGTH_LONG).show();
        }
        else{
            User user = new User();
            user.email = email;
            user.password = pass;
            user.building = building_selected;
            user.floor = floor_selected;

            viewModel.insertUser(user);
            result_code = RESULT_OK;
        }
        return result_code;
    }

    private static boolean isValidPassword(String password) {

        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private int loginUser(){
        int result_code = RESULT_CANCELED;

        String email = add_email.getText().toString();
        String pass = add_pass.getText().toString();
        if (viewModel.authenticate(email, pass)){
            result_code = RESULT_OK;
        }

        return result_code;
    }

}