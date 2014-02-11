package droidz.helloworld;

import droidz.game.DroidzActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class HelloWorldActivity extends Activity {
	
	private static final String TAG = HelloWorldActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello_world, menu);
        return true;
    }
    
    public void onPlayBtnClicked(View view) {
    	Log.d(TAG, "Play Btn Clicked");
    	Intent intent = new Intent(HelloWorldActivity.this, DroidzActivity.class);
    	startActivity(intent);
    }
    
}
