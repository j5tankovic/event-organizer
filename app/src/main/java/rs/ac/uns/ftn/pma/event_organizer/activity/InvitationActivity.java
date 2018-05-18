package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

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
    public static Invitation invitation;
    public static int invitationId=0;
    public static List<Invitation> testDataInvitations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        Toolbar toolbar=(Toolbar) findViewById(R.id.event_toolbar);
        toolbar.setTitle("Invitation");
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayoutInvitation);
        viewPager = findViewById(R.id.pagerInvitation);

        invitationId=(int)getIntent().getIntExtra("position", 0);   //pozicija u listi
        invitation=findInvitationById(invitationId);

        tabAdapter = new TabAdapterInvitationOverview(this);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static List<Invitation> testDataInvitation(){
        ArrayList<Invitation> all=new ArrayList<>();

        Event e1=new Event();
        e1.setId(1);
        e1.setName("Rodjendan");
        e1.setDescription("neki opis");
        e1.setStartDateTime(new Date());
        e1.setEndDateTime(new Date());

        Event e2=new Event();
        e2.setId(2);
        e2.setName("Rostilj");
        e2.setDescription("neki opis 2 2 2");
        e2.setStartDateTime(new Date());
        e2.setEndDateTime(new Date());

        Event e3=new Event();
        e3.setId(3);
        e3.setName("Nova Godina");
        e3.setDescription("neki opis 3 3 3");
        e3.setStartDateTime(new Date());
        e3.setEndDateTime(new Date());

        User user1=new User(1,"user1","user1","user1@gmail.com","user1","user1",null, null);
        User user2=new User(2,"user2","user2","user2@gmail.com","user2","user2",null, null);
        User user3=new User(3,"user3","user3","user3@gmail.com","user3","user3",null, null);

        //event 1: rodjendan -> 3 user:1,2,3
        //event 2: rostilj -> 1 user: 2
        //event 3: nova godina -> 1 user: 2

        Invitation i1=new Invitation();
        i1.setId(1);
        i1.setEvent(e1);
        i1.setInvitedUser(user1);

        Invitation i2=new Invitation();
        i2.setId(2);
        i2.setEvent(e1);
        i2.setInvitedUser(user2);

        Invitation i3=new Invitation();
        i3.setId(3);
        i3.setEvent(e1);
        i3.setInvitedUser(user3);

        Invitation i4=new Invitation();
        i4.setId(4);
        i4.setEvent(e2);
        i4.setInvitedUser(user2);

        Invitation i5=new Invitation();
        i5.setId(5);
        i5.setEvent(e3);
        i5.setInvitedUser(user2);

        all.add(i1);
        all.add(i2);
        all.add(i3);
        all.add(i4);
        all.add(i5);

        testDataInvitations=all;

        return all;
    }
    private Invitation findInvitationById(int id){

        Invitation i=null;
        for(Invitation inv:testDataInvitations){
            if (inv.getEvent().getId()==id) {
                i=inv;
                break;
            }
        }
        return i;
    }
}
