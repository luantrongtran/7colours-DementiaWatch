package ifn372.sevencolors.dementiawatch.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ifn372.sevencolors.dementiawatch.PatientManager;
import ifn372.sevencolors.dementiawatch.R;

/**
 * Created by lua on 5/09/2015.
 */
public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.LeftMenuViewHolder> {
    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    private int patientIcon;

    private String name;
    private int profile;
    private String email;

    LeftMenuAdapter(String Name, String Email, int Profile) {
        patientIcon = R.drawable.patient_bullet;
        name = Name;
        email = Email;
        profile = Profile;
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
        }
        return null;

    }

    @Override
    public void onBindViewHolder(LeftMenuViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(MapsActivity.patientManager.getPatientList().getItems().get(position - 1).getFullName());
            holder.imageView.setImageResource(patientIcon);
        } else {
            holder.profile.setImageResource(profile);
            holder.Name.setText(name);
            holder.email.setText(email);
        }
    }

    @Override
    public int getItemCount() {
        return MapsActivity.patientManager.getPatientList().getItems().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class LeftMenuViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;

        public LeftMenuViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
            } else {
                Name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = 0;
            }
        }
    }
}
