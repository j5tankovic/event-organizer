package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;

public class PeopleOverviewAdapter extends RecyclerView.Adapter<PeopleOverviewAdapter.ViewHolder> {

    private List<Invitation> invitations;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userEmail;
        public ImageView img;

        public ViewHolder(View v) {
            super(v);
            userEmail = (TextView) v.findViewById(R.id.people_user_email);
            img=(ImageView)v.findViewById(R.id.invitations_statusImageButton);
        }
    }

    public PeopleOverviewAdapter(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    @Override
    public PeopleOverviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_email_view, parent, false);
        PeopleOverviewAdapter.ViewHolder vh = new PeopleOverviewAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleOverviewAdapter.ViewHolder holder, int position) {
        int pos=position+1;

        holder.userEmail.setText(pos+". "+invitations.get(position).getInvitedUser().getEmail());

        if(invitations.get(position).getStatus().equals(InvitationStatus.ACCEPTED))
            holder.img.setImageResource(R.drawable.ic_status_accepted_24dp);
        if(invitations.get(position).getStatus().equals(InvitationStatus.REJECTED))
            holder.img.setImageResource(R.drawable.ic_status_rejected_24dp);
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }
}
