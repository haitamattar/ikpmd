package com.school.haitamelattar.freeob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.school.haitamelattar.freeob.model.Advert;
import com.school.haitamelattar.freeob.model.Category;
import com.school.haitamelattar.freeob.view.CategoryAdapter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddAdvertActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Spinner categorySpinner;
    private Button imageBtn;
    private Button saveBtn;
    private EditText titleText, descText;
    private CategoryAdapter adapter;
    private final String getCategoryUrl = "http://school.haitamattar.com/categories"; //school.haitamattar.com
    private final String postNewAdvert = "http://school.haitamattar.com/addAdvert";
    private String encodedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advert);

        // Setting for navigation menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerAddAdvert);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationAddAdvert);
        navigationView.setCheckedItem(R.id.navAddAdvert);
        setupDrawerContent(navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // End settings for navigation menu

        imageBtn = (Button) findViewById(R.id.AddPictureBtn);
        saveBtn = (Button) findViewById(R.id.AddAdvertNextStep);
        titleText = (EditText) findViewById(R.id.addAdvertTitleText);
        descText = (EditText) findViewById(R.id.addAdvertDescText);
        categorySpinner = (Spinner) findViewById(R.id.addAdvertCategorySpin);

        // Fill the categorySpinner (dropdown) with category data
        StringRequest request = new StringRequest(getCategoryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                final ArrayList<Category> categories = gson.fromJson(response,
                        new TypeToken<List<Category>>(){}.getType());

                adapter = new CategoryAdapter(AddAdvertActivity.this,
                        R.layout.content_category_spinner, R.id.spinnerItem, categories);

                categorySpinner.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAdvertActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        // Save advert
        saveBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleText.getText().toString().length() <= 0) {
                    titleText.setError("Enter a title");
                } else if (descText.getText().toString().length() <= 0) {
                    descText.setError("Enter a description");
                } else if (encodedImage == null) {
                    Snackbar.make(drawerLayout, "Kies eerst een afbeelding!",
                            Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Category cat = (Category) categorySpinner.getSelectedItem();
                    addAdvert(
                            titleText.getText().toString(),
                            descText.getText().toString(),
                            cat.getId().toString(),
                            cat.getName(),
                            encodedImage
                    );
                }
            }
        }));

        imageBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, 7);
            }
        }));
    }

    public void addAdvert(final String title, final String description, String categoryId,
                          final String categoryName, final String encodedImage) {
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String token = settings.getString("loginToken", "");
        String email = settings.getString("email", "");
        final String userId = settings.getString("id", "");


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("authToken", token);
        params.put("advert_name", title);
        params.put("description", description);
        params.put("category", categoryId);
        params.put("image", encodedImage);

        // Fill the categorySpinner (dropdown) with category data
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postNewAdvert, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Advert currentAdvert = new Advert("0", userId, title, description, "", categoryName, encodedImage);

                Intent detailIntent = new Intent(AddAdvertActivity.this,
                        AdvertDetailActivity.class);
                detailIntent.putExtra("Advert", currentAdvert);

                startActivity(detailIntent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAdvertActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the navigation menu Items on AddAdvertActivity
     * @param navigationView The navigation menu view
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        Intent intent;

                        switch (menuItem.getItemId()){
                            case R.id.navAdverts:
                                intent = new Intent(AddAdvertActivity.this, AdvertsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navAddAdvert:
                                return false;
                            case R.id.navProfile:
                                intent = new Intent(AddAdvertActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navSettings:
                                intent = new Intent(AddAdvertActivity.this, SettingActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream;

                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Encode the Bitmap to Base64. Image will be resized with resizeBitmap!
     * @param bm bitmap
     * @return Base64 encoded String
     */
    private String encodeImage(Bitmap bm)
    {
        bm = resizeBitmap(bm, 640);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    /**
     * Resize a Bitmap to a maximum size.
     * @param image the Bitmap
     * @param maxSize the maximum size
     * @return The resized image
     */
    public Bitmap resizeBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
