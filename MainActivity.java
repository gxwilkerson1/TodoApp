package com.conerstonetodo;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //variable declaration
    public static ArrayList<String> todoList = new ArrayList<>();
    public static ArrayAdapter<String> todoAdapter;
    private ListView todoView;
    private SQLiteDatabase myDB;


    //static method to update item of todoList from Edit acvtivity
    public static void updateList(int index, String value) {
        todoList.set(index, value);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate the listview of the UI
        todoView = (ListView) findViewById(R.id.todoView);

        //instantiate arrayAdapter using todoList
        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoList);

        //set the adapter of the listview to the todoAdapter
        todoView.setAdapter(todoAdapter);

        //create DB and table to hold todoItems
        myDB = this.openOrCreateDatabase("Todo", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS todoItems (id SERIAL PRIMARY KEY, item VARCHAR(50))");

        //update listview from last saved todoitems in Db
        updateListView();
        todoAdapter.notifyDataSetChanged();



        //onclick listener opens editTodo activity
        //sends clicked on item to editTodo activity
        todoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent sendItem = new Intent(getApplicationContext(), EditTodo.class);
                sendItem.putExtra("editItem", todoList.get(i));
                sendItem.putExtra("index", i);
                startActivity(sendItem);
            }
        });

        //LongClick listener deletes item from the todoList
        //updates adapter
        todoView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                todoList.remove(todoList.get(i));
                todoAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    /*
    addTodo is called on the AddButton click
    this adds the current value of the editText field to the todoList and updates the list view adapter

     */
    public void addTodo(View view){



        EditText todoEdit = (EditText) findViewById(R.id.editTodo);
        String todoString = todoEdit.getText().toString();

        if (todoString.isEmpty()){
            return;
        }

        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoList);
        todoView.setAdapter(todoAdapter);
        updateItems(todoString);

        todoAdapter.notifyDataSetChanged();




        todoEdit.setText("");


    }

    /*
    this method is called in the onCreate method to load the todoItems from the DB that was saved
    in the onStop method when the app last stopped
     */
    public void updateListView(){
        Cursor c = myDB.rawQuery("SELECT * FROM todoItems", null);
        c.moveToFirst();

        if(c.getCount() == 0){
            return;
        }

        int itemIndex = c.getColumnIndex("item");

        while(!c.isAfterLast()){
            todoList.add(c.getString(itemIndex));
            c.moveToNext();
        }



    }

    //this method adds strings to the todoList arrayList
    public void updateItems(String i){

        todoList.add(i);
    }

    /*
    this method updates the DB to contain only the current items in the todoList
    to be opened in the onCreate method next time the app opens
    */
    @Override
    protected void onStop() {

        myDB.execSQL("DELETE FROM todoItems");
        for (String i: todoList) {
            String query = String.format("INSERT INTO todoItems (item) VALUES ('%s')", i);
            myDB.execSQL(query);
        }



        super.onStop();

    }
}