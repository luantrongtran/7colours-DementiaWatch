/**
 * References http://www.android4devs.com/2015/01/recycler-view-handling-onitemtouch-for.html
 */
package ifn372.sevencolors.dementiawatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.dementiawatch.BitMapUtils;
import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.dementiawatch.PatientManager;
import ifn372.sevencolors.dementiawatch.R;

public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.LeftMenuViewHolder> {
    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    private static final int TYPE_FOOTER = 2;

    Activity context;
    Drawable patientIcon;
    UserInfoPreferences userInfoPreferences;
    private String name;
//    private int profile;
//    private String email;

    LeftMenuAdapter(Activity context) {
        this.context = context;

        userInfoPreferences = new UserInfoPreferences(context);
        name = userInfoPreferences.getFullName();
//        email = userInfoPreferences.getEmail();

        //Modify later, should be url string not R.drawable.*
        //profile = R.drawable.app_logo;//userInfoPreferences.getProfilePicture();

        this.patientIcon = context.getResources()
                .getDrawable(R.drawable.ic_room_black_24dp);
    }

    @Override
    public LeftMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

            LeftMenuViewHolder vhItem = new LeftMenuViewHolder(v, viewType);

            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);

            LeftMenuViewHolder vhHeader = new LeftMenuViewHolder(v, viewType);

            return vhHeader;
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_patient_item, parent, false);

            LeftMenuViewHolder vhFooter = new LeftMenuViewHolder(v, viewType);

            return vhFooter;
        }
        return null;

    }

    @Override
    public void onBindViewHolder(LeftMenuViewHolder holder, final int position) {
        final int itemIndex = position - 1; //because of excluding the header
        if (holder.Holderid == TYPE_ITEM) {
            if(MapsActivity.patientManager.getPatientList().getItems() == null) {
                return;
            }

            final Patient patient = MapsActivity.patientManager
                    .getPatientList().getItems().get(itemIndex);
            View.OnTouchListener onTouchListener = new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent(context, PatientSettingActivity.class);
                    intent.putExtra(Constants.create_new_fence_intent_data, patient.getId());
                    context.startActivityForResult(intent, MapsActivity.PATIENT_SETTING_REQUEST_CODE);
                    return false;
                }
            };

            holder.textView.setText(patient.getFullName());

            holder.imageView.setImageBitmap(BitMapUtils
                    .getMutableBitmapFromResourceFromResource(patientIcon,
                            PatientManager.patientColors[itemIndex]));


            final MapsActivity mapsActivity = (MapsActivity)context;
            holder.findLocationButton.setTag("findLocation");
            holder.findLocationButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.i(Constants.application_id, "item " + itemIndex + "clicked");
                    LatLng latLng = new LatLng(patient.getCurrentLocation().getLat(),
                            patient.getCurrentLocation().getLon());
                    mapsActivity.panToLatLng(latLng, true);
                    return true;
                }
            });

            holder.settingButton.setOnTouchListener(onTouchListener);
        } else if (holder.Holderid == TYPE_FOOTER){

            if(userInfoPreferences.getRole() == UserInfoPreferences.CARER_ROLE
                    && MapsActivity.patientManager.getPatientList().size() == PatientManager.MAX_NUM_OF_PATIENTS) {
                holder.imageView.setVisibility(View.GONE);
                holder.textView.setVisibility(View.GONE);
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.VISIBLE);
            }
            View.OnTouchListener touchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent(context, AddNewPatientActivity.class);
                    context.startActivityForResult(intent, MapsActivity.PATIENT_SETTING_REQUEST_CODE);
                    return false;
                }
            };

            holder.imageView.setOnTouchListener(touchListener);
            holder.textView.setOnTouchListener(touchListener);
        } else {
//            holder.profile.setImageResource(profile);
            holder.Name.setText(name);
//            holder.email.setText(email);
        }
    }

    @Override
    public int getItemCount() {
        if(MapsActivity.patientManager.getPatientList().getItems() == null)
            return 0;

        return MapsActivity.patientManager.getPatientList().getItems().size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)){
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
    private boolean isPositionFooter(int position) {return position == getItemCount()-1;}

    public static class LeftMenuViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        //for items
        TextView textView;
        ImageView imageView;
        ImageView findLocationButton;
        ImageView settingButton;

        //For header
//        ImageView profile;
        TextView Name;
//        TextView email;

        public LeftMenuViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                findLocationButton = (ImageView) itemView.findViewById(R.id.findLocationButton);
                settingButton = (ImageView)itemView.findViewById(R.id.settingButton);
                Holderid = 1;
            } else if (ViewType == TYPE_FOOTER) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 2;
            } else {
                Name = (TextView) itemView.findViewById(R.id.name);
                Holderid = 0;
            }
        }
    }
}