package com.tanusha.ideasgenerator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    ;
    private Intent shareIntent;
    private ShareActionProvider shareProvider;
    private Menu mMenu;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void resetUpdating() {
        // Get our refresh item from the menu
        MenuItem m = mMenu.findItem(R.id.action_refresh);
        if (m.getActionView() != null) {
            // Remove the animation.
            generateImage();
            m.getActionView().clearAnimation();
            m.setActionView(null);
        }
    }


    //Restore image after device rotation
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String imageUri = savedInstanceState.getString("imageUri", null);
        if (imageUri != null) {
            parseGalleryData(Uri.parse(imageUri));
        }
    }

    //Save uri to restore last image after rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mUri != null) {
            outState.putString("imageUri", mUri.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mMenu = menu;

        MenuItem item = menu.findItem(R.id.menu_item_share);

        shareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareImage();

        if (mUri == null) {
            generateImage();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh, null);
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);
            item.setActionView(iv);
            new UpdateTask(this).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void parseGalleryData(Uri imageUri) {
        InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            mImageView.setImageBitmap(selectedImage);
            mUri = imageUri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void generateImage() {

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int colorTxt = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        if (color == colorTxt) {
            generateImage();
            return;
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Bitmap newImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Canvas c = new Canvas(newImage);
        c.drawColor(color);

        TextPaint paintTxt = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintTxt.setColor(colorTxt);
        paintTxt.setTextSize(80);
        paintTxt.setTextAlign(Paint.Align.CENTER);
        paintTxt.setShadowLayer(1f, 0f, 1f, Color.BLACK);

        int textWidth = c.getWidth() - 80;

        String[] firstPart = getResources().getStringArray(R.array.first_part);
        String[] secondPart = getResources().getStringArray(R.array.second_part);

        String str = firstPart[rnd.nextInt(29)] + " " + secondPart[rnd.nextInt(29)];

        StaticLayout layout = new StaticLayout(str, paintTxt, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

        int textHeight = layout.getHeight();

        float x = (newImage.getWidth()) / 2;
        float y = (newImage.getHeight() - textHeight) / 2;

        c.save();
        c.translate(x, y);
        layout.draw(c);
        c.restore();

        mImageView.setImageBitmap(newImage);

        Uri imageUri = getLocalBitmapUri(mImageView);
        if (imageUri != null) {
            parseGalleryData(imageUri);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            shareProvider.setShareIntent(shareIntent);

        }
    }


    private void shareImage() {

        Uri bmpUri = getLocalBitmapUri(mImageView);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Генератор идей для GDG");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

            if (shareProvider != null) {
                shareProvider.setShareIntent(shareIntent);
            }
            // Launch sharing dialog for image
            //startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }
    }
}
