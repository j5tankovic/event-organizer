package rs.ac.uns.ftn.pma.event_organizer;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rs.ac.uns.ftn.pma.event_organizer.adapter.TabAdapter;

public class EventOverviewActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_overview);

        tabLayout = findViewById(R.id.event_tab);
        viewPager = findViewById(R.id.event_pager);

        tabAdapter = new TabAdapter(this);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
