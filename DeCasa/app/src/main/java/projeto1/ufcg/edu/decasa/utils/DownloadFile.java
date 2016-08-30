package projeto1.ufcg.edu.decasa.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Andreza on 30/08/2016.
 */
public class DownloadFile extends AsyncTask<String, Void, Bitmap> {

    private CircularImageView imageView;


    public DownloadFile(CircularImageView imageView) {
        if (imageView == null ) {
            throw new NullPointerException();
        }
        this.imageView = imageView;
    }


    @Override
    protected void onPreExecute(){
        Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: " + Thread.currentThread().getName());
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap imagemBitmap = null;
        try{
            Log.i("AsyncTask", "Baixando a imagem Thread: " + Thread.currentThread().getName());

            imagemBitmap = Utils.downloadImage(params[0]);

        }catch (IOException e){
            Log.i("AsyncTask", e.getMessage());
        }

        return imagemBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        if(bitmap!=null) {
            imageView.setImageBitmap(bitmap);
            Log.i("AsyncTask", "Exibindo Bitmap Thread: " + Thread.currentThread().getName());
        }else{
            Log.i("AsyncTask", "Erro ao baixar a imagem " + Thread.currentThread().getName());
        }
        Log.i("AsyncTask", "Tirando ProgressDialog da tela Thread: " + Thread.currentThread().getName());
    }
}
