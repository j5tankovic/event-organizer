package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.TabAdapterInvitationOverview;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class InvitationActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TabAdapterInvitationOverview tabAdapter;
    private ViewPager viewPager;
    public static Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        Toolbar t = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        event = (Event) getIntent().getExtras().get(InvitationsActivity.EVENT);

        tabLayout = findViewById(R.id.tabLayoutInvitation);
        viewPager = findViewById(R.id.pagerInvitation);

        tabAdapter = new TabAdapterInvitationOverview(this);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}