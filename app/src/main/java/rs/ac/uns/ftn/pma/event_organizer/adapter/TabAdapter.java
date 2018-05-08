package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import rs.ac.uns.ftn.pma.event_organizer.fragment.PeopleOverviewFragment;
import rs.ac.uns.ftn.pma.event_organizer.fragment.PlaceOffersFragment;
import rs.ac.uns.ftn.pma.event_organizer.fragment.ShoppingListFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private static final int MAX_TABS = 4;

    private Context context;

    public TabAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.context = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PlaceOffersFragment(); //FIXME change this for general fragment
            case 1:
                return new PlaceOffersFragment();
            case 2:
                return new ShoppingListFragment();
            case 3:
                return new PeopleOverviewFragment();
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
                return "Places";
            case 2:
                return "Shopping list";
            case 3:
                return "People";
            default:
                throw new RuntimeException("Invalid number of tabs");
        }
    }
}
