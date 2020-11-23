package com.example.a2tuckj82.myshoppinglist;

        import android.database.Cursor;
        import android.media.MediaPlayer;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//Connection to the database helper to mainA and define the strings IDs
    DatabaseHelper db;

    Button add_data;
    Button b_delete;
    EditText add_item;

    ListView users_list;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

//function to allow sound to play when the toast is passed via add and delete
    private void playDing(int resId){
        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.ding);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }
//this function sets up the find view by id
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        listItem = new ArrayList<>();
        add_data = findViewById(R.id.add_data);
        add_item = findViewById(R.id.add_item);
        b_delete = findViewById(R.id.button_delete);
        users_list = findViewById(R.id.users_list);

        db.EmptyData("label");
        viewData();
        deleteData();

//function to set up list view
        users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                String text = users_list.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, ""+text, Toast.LENGTH_SHORT).show();
             }
        });
//function to add data to the database
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = add_item.getText().toString();
                if (!name.equals("") && db.insertData(name)){
                    Toast.makeText(MainActivity.this, "Item added to your shopping list", Toast.LENGTH_SHORT).show();
                    add_item.setText("");
                    viewData();
                } else {
                    Toast.makeText(MainActivity.this, "No item was added to your shopping list", Toast.LENGTH_LONG).show();
                    playDing(R.raw.ding);
                }
            }
        });
    }
//functions checks the database to collect any recorded data and display it on the list viewer
    private void viewData() {
        Cursor cursor = db.viewData();
//refreshes the list viewer
        listItem.clear();

        if (cursor.getCount() == 0){
            Toast.makeText(this, "You currently have no items on your shopping list", Toast.LENGTH_LONG).show();
        } else{
            while (cursor.moveToNext()){
                listItem.add(cursor.getString(1));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            users_list.setAdapter(adapter);
        }
    }
 //delete function to allow the user to remove item by name and deletes it from the database
    public void deleteData(){
        b_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Integer deleterRows = db.DeleteData(add_item.getText().toString());
                if (deleterRows > 0) {
                    Toast.makeText(MainActivity.this, "Item was removed", Toast.LENGTH_LONG).show();
                    viewData(); //this allows it to trigger the view data function to update the list view and remove the item live
                } else
                    Toast.makeText(MainActivity.this, "No item was removed", Toast.LENGTH_LONG).show();
                playDing(R.raw.ding);
            }
        });
    }
}
