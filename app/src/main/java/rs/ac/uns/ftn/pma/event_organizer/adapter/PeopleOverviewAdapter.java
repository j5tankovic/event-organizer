package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.R;

public class PeopleOverviewAdapter extends RecyclerView.Adapter<PeopleOverviewAdapter.ViewHolder> {

    private List<User> testData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userEmail;

        public ViewHolder(View v) {
            super(v);
            userEmail = (TextView) v.findViewById(R.id.people_user_email);
        }
    }

    public PeopleOverviewAdapter(List<User> testData) {
        this.testData = testData;
    }

    @Override
    public PeopleOverviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_view, parent, false);
        PeopleOverviewAdapter.ViewHolder vh = new PeopleOverviewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleOverviewAdapter.ViewHolder holder, int position) {
        int pos=position+1;
        holder.userEmail.setText(pos+". "+testData.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }
}
