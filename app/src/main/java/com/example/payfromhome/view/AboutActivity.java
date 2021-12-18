package com.example.payfromhome.view;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.payfromhome.R;
import com.example.payfromhome.databinding.ActivityAboutBinding;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        setSupportActionBar(binding.toolbarAbout);
        setSupportActionBar(binding.toolbarAbout);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.about_us));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
                symbolLayerIconFeatureList.add(Feature.fromGeometry(
                        Point.fromLngLat(107.48333, -6.83333)));

                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                        .withImage(ICON_ID, BitmapFactory.decodeResource(
                                AboutActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))
                        .withSource(new GeoJsonSource(SOURCE_ID,
                                FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
                        .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                .withProperties(
                                        iconImage(ICON_ID),
                                        iconAllowOverlap(true),
                                        iconIgnorePlacement(true)
                                )
                        ), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }
}