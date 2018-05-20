package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class PeopleInvitationAdapter extends RecyclerView.Adapter<PeopleInvitationAdapter.ViewHolder> {

private List<User> testData;

public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView userEmail;
    public ImageView img;

    public ViewHolder(View v) {
        super(v);
        userEmail = (TextView) v.findViewById(R.id.invitation_user_email_tv);
        img=(ImageView)v.findViewById(R.id.invitation_statusImageButton);

    }
}

    public PeopleInvitationAdapter(List<User> testData) {
        this.testData = testData;
    }

    @Override
    public PeopleInvitationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invitation_user_email, parent, false);
        PeopleInvitationAdapter.ViewHolder vh = new PeopleInvitationAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleInvitationAdapter.ViewHolder holder, int position) {
        holder.userEmail.setText(testData.get(position).getEmail());
        int pos=position+1;
        holder.userEmail.setText(pos+". "+testData.get(position).getEmail());
        //src - deppending on status of invitation
        if(position==1)
            holder.img.setImageResource(R.drawable.ic_status_accepted_24dp);
        if(position==3)
            holder.img.setImageResource(R.drawable.ic_status_rejected_24dp);

    }

    @Override
    public int getItemCount() {
        return testData.size();
    }
}
