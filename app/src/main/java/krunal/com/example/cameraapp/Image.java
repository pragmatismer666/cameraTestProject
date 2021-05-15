package krunal.com.example.cameraapp;

import java.io.Serializable;

public class Image implements Serializable {
    private String imagePath;
    private String comment;

    public Image(String imagePath, String comment) {
        this.imagePath = imagePath;
        this.comment = comment;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getComment() {
        return comment;
    }
}
