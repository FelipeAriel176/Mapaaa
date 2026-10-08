package com.example.mapa2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView osmMapView = null;
    private GoogleMap googleMap = null;
    private View googleMapContainer;
    private Marker markerSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue("Mapa/ benja@gmail.com");
        setContentView(R.layout.activity_main);

        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnOnstreet = findViewById(R.id.btnOnstreet);

        osmMapView = findViewById(R.id.osmdroidMapView);
        osmMapView.setMultiTouchControls(true);
        osmMapView.setTileSource(TileSourceFactory.MAPNIK);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            googleMapContainer = mapFragment.getView();
        }

        GeoPoint startPoint = new GeoPoint(-33.498895, -70.616617);
        GeoPoint punto2 = new GeoPoint(-33.498720, -70.616130);
        GeoPoint punto3 = new GeoPoint(-33.498561, -70.615666);

        osmMapView.getController().setZoom(18.0);
        osmMapView.getController().setCenter(startPoint);
        Toast.makeText(this, "Mapa listo con Google y Onstreet", Toast.LENGTH_SHORT).show();

        int anchoIcono = 64;
        int altoIcono = 64;

        Marker maker = new Marker(osmMapView);
        maker.setPosition(startPoint);
        maker.setTitle("Punto Inicial");
        maker.setSnippet("Ubicación de origen");

        Marker maker2 = new Marker(osmMapView);
        maker2.setPosition(punto2);
        try {
            android.graphics.Bitmap b = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.vendedor);
            android.graphics.Bitmap bitmapRedimensionado = android.graphics.Bitmap.createScaledBitmap(b, anchoIcono, altoIcono, false);
            android.widget.ImageView iv = new android.widget.ImageView(this);
            maker2.setIcon(new android.graphics.drawable.BitmapDrawable(getResources(), bitmapRedimensionado));
        } catch (Exception e) {
            Log.e("MAPA", "Error al ajustar icono vendedor: " + e.getMessage());
        }
        maker2.setTitle("Vendedor Ambulante");
        maker2.setSnippet("Venta de productos en la calle");

        Marker maker3 = new Marker(osmMapView);
        maker3.setPosition(punto3);
        try {
            android.graphics.Bitmap b = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.pizza);
            android.graphics.Bitmap bitmapRedimensionado = android.graphics.Bitmap.createScaledBitmap(b, anchoIcono, altoIcono, false);
            maker3.setIcon(new android.graphics.drawable.BitmapDrawable(getResources(), bitmapRedimensionado));
        } catch (Exception e) {
            Log.e("MAPA", "Error al ajustar icono: " + e.getMessage());
        }
        maker3.setTitle("Pizzeria");


        osmMapView.getOverlays().add(maker);
        osmMapView.getOverlays().add(maker2);
        osmMapView.getOverlays().add(maker3);



        MapEventsReceiver puntoSelecionado = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double lat = p.getLatitude();
                double lon = p.getLongitude();
                Log.d("MAPA", "Latitud " + lat + " Longitud" + lon);

                if (markerSeleccionado != null) {
                    osmMapView.getOverlays().remove(markerSeleccionado);
                }

                markerSeleccionado = new Marker(osmMapView);
                markerSeleccionado.setPosition(p);
                markerSeleccionado.setTitle("ACA");
                markerSeleccionado.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                osmMapView.getOverlays().add(markerSeleccionado);
                osmMapView.invalidate();
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) { return false; }
        };
        osmMapView.getOverlays().add(new MapEventsOverlay(puntoSelecionado));
        osmMapView.invalidate();

        btnGoogle.setOnClickListener(v -> {
            if (googleMapContainer != null) googleMapContainer.setVisibility(View.VISIBLE);
            osmMapView.setVisibility(View.GONE);
        });

        btnOnstreet.setOnClickListener(v -> {
            if (googleMapContainer != null) googleMapContainer.setVisibility(View.GONE);
            osmMapView.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;
        LatLng startLatLng = new LatLng(-33.498895, -70.616617);
        googleMap.addMarker(new MarkerOptions().position(startLatLng).title("Hola - Repartidor"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 17.0f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (osmMapView != null) osmMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (osmMapView != null) osmMapView.onPause();
    }
}
