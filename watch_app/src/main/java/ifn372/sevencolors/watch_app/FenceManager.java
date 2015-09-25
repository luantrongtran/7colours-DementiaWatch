package ifn372.sevencolors.watch_app;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.FenceSharedPreferences;

/**
 * Created by lua on 24/09/2015.
 */
public class FenceManager {
    Context context;
    GoogleMap gMap;

    List<Circle> circles;
    public FenceManager (Context context, GoogleMap googleMap) {
        this.context = context;
        this.gMap = googleMap;
        circles = new ArrayList<>();
    }

    public void updateFences() {
        int fenceColor = context.getResources().getColor(R.color.fence_color);
        FenceSharedPreferences fenceSharedPreferences = new FenceSharedPreferences(context);
        FenceList fenceList = fenceSharedPreferences.getFences();
        if(fenceList == null || fenceList.getItems() == null) {
            return;
        }

        for(Circle circle : circles) {
            circle.remove();
        }
        circles.clear();

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.fillColor(fenceColor).strokeColor(fenceColor);
        for(Fence fence : fenceList.getItems()) {
            circleOptions = circleOptions.radius(fence.getRadius()).center(
                    new LatLng(fence.getLat(), fence.getLon()));

            Circle c = gMap.addCircle(circleOptions);
            circles.add(c);
        }
    }
}
