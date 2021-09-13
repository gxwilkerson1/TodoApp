package com.conerstonetodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditTodo extends AppCompatActivity {


    EditText editText;

    /*
    When this activity is created the editText field is populated from the clicked item in the
    Main activity listView
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        editText = (EditText) findViewById(R.id.EditTodo);

        Intent intent = getIntent();
        String item = intent.getStringExtra("editItem");
        editText.setText(item);


    }

    /*

    this method is called when the Edit button is clicked
    if the editText is empty when this method is called the todoItem is not updated
    otherwise the todoItem is updated in the Main activity with the current value of the Edittext

     */

    public void EditButton(View view){


        editText = (EditText) findViewById(R.id.EditTodo);

        String item = editText.getText().toString();

        if (item.isEmpty()){
            return;
        }

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);

        MainActivity.updateList(index, item);

        MainActivity.todoAdapter.notifyDataSetChanged();

        this.finish();

    }
}