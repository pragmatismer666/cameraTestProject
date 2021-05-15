package krunal.com.example.cameraapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView comment;
    public ImageHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        comment = itemView.findViewById(R.id.commentTextView);
    }
}
