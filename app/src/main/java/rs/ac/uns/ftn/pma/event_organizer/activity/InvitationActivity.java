package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.TabAdapterInvitationOverview;

public class InvitationActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TabAdapterInvitationOverview tabAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        Toolbar toolbar=(Toolbar) findViewById(R.id.event_toolbar);
        toolbar.setTitle("Invitation");
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayoutInvitation);
        viewPager = findViewById(R.id.pagerInvitation);

        tabAdapter = new TabAdapterInvitationOverview(this);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
