package krunal.com.example.cameraapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

    private ArrayList<Image> images;

    public ImageAdapter(ArrayList<Image> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row_item, parent, false);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Image image = images.get(position);
        holder.comment.setText(image.getComment());
        File imgFile = new File(image.getImagePath());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            holder.imageView.setImageBitmap(myBitmap);

        }
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }
}
