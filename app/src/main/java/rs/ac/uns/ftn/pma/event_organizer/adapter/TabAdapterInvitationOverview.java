package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import rs.ac.uns.ftn.pma.event_organizer.fragment.GeneralInvitationFragment;
import rs.ac.uns.ftn.pma.event_organizer.fragment.PeopleInvitationFragment;

public class TabAdapterInvitationOverview extends FragmentStatePagerAdapter {

    private static final int MAX_TABS = 2;

    private Context context;

    public TabAdapterInvitationOverview(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.context = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GeneralInvitationFragment();
            case 1:
                return new PeopleInvitationFragment();
            default:
                throw new RuntimeException("Invalid number of tabs");
        }
    }

    @Override
    public int getCount() {
        return MAX_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "General";
            case 1:
                return "People";
            default:
                throw new RuntimeException("Invalid number of tabs");
        }
    }
}
