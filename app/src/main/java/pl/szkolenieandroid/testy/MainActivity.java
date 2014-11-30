package pl.szkolenieandroid.testy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import pl.szkolenieandroid.testy.events.UpdateMessageListEvent;


public class MainActivity extends ActionBarActivity {


    private final String MESSAGE = "Message";
    @InjectView(R.id.editText)
    EditText text;
    private EventBus bus = EventBus.getDefault();

    @InjectView(R.id.listView)
    ListView listView;
    private List<String> messages = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
        listView.setAdapter(adapter);
    }

    private void updateMessages() {
        Log.e("###", "UpdateMessages");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(MESSAGE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e != null) {
                    Log.e("MainActiviity", "Error: " + e);
                } else {
                    Log.i("MainActivity", "Size: " + parseObjects.size());
                    messages.clear();
                    for (ParseObject parseObject : parseObjects) {
                        messages.add(0, parseObject.getString("text"));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick(R.id.button)
    public void click() {

        ParseObject parseObject = new ParseObject("Message");
        parseObject.put("text", text.getText().toString());
        parseObject.saveInBackground();
        ParsePush push = new ParsePush();
        push.setChannel("Giants");
        push.setMessage(text.getText().toString());
        push.sendInBackground();

        text.setText("");
    }

    public void onEvent(UpdateMessageListEvent updateMessageListEvent) {
        updateMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMessages();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

}
