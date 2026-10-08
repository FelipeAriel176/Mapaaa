package com.example.mapa2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private MapView map = null;
    private Marker markerSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Configuration.getInstance().setUserAgentValue("Mapa/ benja@gmail.com");

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.WIKIMEDIA);
        map.setMultiTouchControls(true); //

        GeoPoint startPoint = new GeoPoint(-33.498895, -70.616617);
        GeoPoint punto2 = new GeoPoint(-33.498720, -70.616130);
        GeoPoint punto3 = new GeoPoint(-33.498561, -70.615666);

        map.getController().setZoom(20.0);
        Toast.makeText(this, "Tengo el codigo de la discodia", Toast.LENGTH_SHORT).show();

        map.getController().setCenter(startPoint);

        Marker maker = new Marker(map);
        maker.setPosition(startPoint);
        maker.setTitle("Hola");
        maker.setSnippet("Repartidor cerca");

        Marker maker2 = new Marker(map);
        maker2.setPosition(punto2);
        maker2.setIcon(
                ContextCompat.getDrawable(
                        this,
                        R.mipmap.ic_launcher_repartidor_round)
        );
        maker2.setTitle("Hola");
        maker2.setSnippet("Repartidor cerca");

        Marker maker3 = new Marker(map);
        maker3.setPosition(punto3);
        maker3.setIcon(
                ContextCompat.getDrawable(
                        this,
                        R.mipmap.ic_launcher_poli)
        );
        maker3.setTitle("Hola");
        maker3.setSnippet("Repartidor cerca");


        map.getOverlays().add(maker);
        map.getOverlays().add(maker2);
        map.getOverlays().add(maker3);
        map.invalidate();


        MapEventsReceiver puntoSelecionado = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double lat = p.getLatitude();
                double lon = p.getLongitude();

                Log.d("MAPA", "Latitud " + lat + " Longitud" + lon);

                if (markerSeleccionado != null ) {
                    map.getOverlays().remove(markerSeleccionado);
                }

                markerSeleccionado = new Marker(map);
                markerSeleccionado.setPosition(p);
                markerSeleccionado.setTitle("ACA");
                markerSeleccionado.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                );
                map.getOverlays().add(markerSeleccionado);
                map.invalidate();

                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay eventsOverlay = new MapEventsOverlay(puntoSelecionado);
        map.getOverlays().add(eventsOverlay);
    }
}