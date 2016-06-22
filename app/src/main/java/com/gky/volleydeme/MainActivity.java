package com.gky.volleydeme;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://gank.io/api/day";

    private DatePickerDialog datePickerDialog;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_body)
    RecyclerView mRecyclerView;

    private DataAdapter mAdapter;

    private Calendar mDate;

    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mDate = Calendar.getInstance(Locale.CHINA);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate.set(year, monthOfYear, dayOfMonth);
                pullData();
            }
        },
        mDate.get(Calendar.YEAR),
        mDate.get(Calendar.MONTH),
        mDate.get(Calendar.DAY_OF_MONTH));

        mLoadingDialog = new ProgressDialog(this);

        mAdapter = new DataAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pullData();
    }

    private void pullData(){
        mLoadingDialog.show();
        DailyRequest request = createDataRequest(mDate);
        NetUtils.getInstance().addToRequestQueue(request);
    }

    private DailyRequest createDataRequest(Calendar date){
        final String url = String.format("%s/%d/%d/%d", BASE_URL,
            date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH));
        DailyRequest dataRequest = new DailyRequest(Request.Method.GET, url,
            new Response.Listener<List<ItemResponseData>>() {
                @Override
                public void onResponse(List<ItemResponseData> response) {
                    mLoadingDialog.dismiss();
                    mAdapter.setDatas(response);
                }
            },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadingDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        dataRequest.setTag(this);
        return dataRequest;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        NetUtils.getInstance().cancelAll(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            datePickerDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
