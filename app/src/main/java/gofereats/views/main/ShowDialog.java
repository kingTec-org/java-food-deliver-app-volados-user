package gofereats.views.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.obs.CustomTextView;

import gofereats.R;


public class ShowDialog extends Activity {

    private TextView tvMessage;
    private TextView tvOk;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_dialog);
        this.setFinishOnTouchOutside(false);

        tvMessage = (CustomTextView) findViewById(R.id.tvMessage);
        tvOk = findViewById(R.id.tvOk);

        tvMessage.setText(getIntent().getStringExtra("message"));
        type = getIntent().getIntExtra("type", 0);


        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getType();
            }
        });
    }

    public void getType() {
        if (type == 5) {
            finish();
        } else if (type == 2 || type == 6) {
            MainActivity.cancelled = true;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("type", "placeorder");
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {

    }
}

