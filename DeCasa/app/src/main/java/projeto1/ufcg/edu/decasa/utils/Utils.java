package projeto1.ufcg.edu.decasa.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Utils {

    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    public static Bitmap downloadImage(String url) throws IOException {
        URL endereco;
        InputStream inputStream;
        Bitmap imagem;

        endereco = new URL(url);
        inputStream = endereco.openStream();
        imagem = BitmapFactory.decodeStream(inputStream);

        inputStream.close();

        return imagem;
    }
}
