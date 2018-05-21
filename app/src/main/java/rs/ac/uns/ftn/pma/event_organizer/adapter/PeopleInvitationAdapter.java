package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class PeopleInvitationAdapter extends RecyclerView.Adapter<PeopleInvitationAdapter.ViewHolder> {

private List<User> testData;

public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView userEmail;

    public ViewHolder(View v) {
        super(v);
        userEmail = (TextView) v.findViewById(R.id.invitation_user_email_tv);
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
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }
}
