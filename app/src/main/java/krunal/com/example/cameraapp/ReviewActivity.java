package krunal.com.example.cameraapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {


    private RecyclerView mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mlist = findViewById(R.id.list);
        DBHelper myDb = new DBHelper(this);
        ArrayList<Image> imageList = myDb.getAllCotacts();
        if(imageList != null) {
            ImageAdapter imageAdapter = new ImageAdapter(imageList);
            mlist.setAdapter(imageAdapter);
        }
    }

}