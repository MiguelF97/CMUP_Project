package pt.ua.smartelevator;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.List;

import pt.ua.smartelevator.data.AppViewModel;
import pt.ua.smartelevator.data.Trip;

public class Home_Page extends AppCompatActivity {

    MqttHelper mqttHelper;
    MqttAndroidClient client;
    AppViewModel viewModel;
    private TripListAdapter tripListAdapter;
    FloatingActionButton callElevator;
    String floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        tripListAdapter = new TripListAdapter();
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        callElevator = findViewById(R.id.callElevator);
        floor =  getIntent().getStringExtra("Floor");
        System.out.println(floor);

        callElevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    Log.d(this.getClass().getName(), "Internet connection NOT available");

                    Toast.makeText(Home_Page.this, "Internet connection NOT available", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println(floor);
                    publishMsg(floor);
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(tripListAdapter);
        viewModel.getAllTrips().observe(this, new Observer<List<Trip>>() {
                    @Override
                    public void onChanged(List<Trip> trips) {
                        tripListAdapter.setTripList(trips);
                    }
                });

        startMqtt();
    }


    private void startMqtt(){
        client = new MqttAndroidClient(this.getApplicationContext(),
                "tcp://broker.hivemq.com:1883",
                "clientId-wusVpAmoEW20");
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("Home_Page", "onSuccess");
                    subscribe();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("Home_Page", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void subscribe(){
        String topic = "smartElevator/elevatorTrips";
        int qos = 2;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("Home_Page", "MQTT connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    viewModel.insertTrip(Trip.fromString(message.toString()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMsg(String msg){
        try {
            String topic = "smartElevator/elevatorCalls";
            byte[] payload = msg.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(payload);
            message.setRetained(true);
            client.publish(topic, message);
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //mqttHelper.publishToTopic(msg);
    }

    private boolean isConnected(){
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());

        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                result = true;
            }
        }
        return result;
    }
}