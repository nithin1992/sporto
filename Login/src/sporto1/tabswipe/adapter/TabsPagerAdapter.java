package sporto1.tabswipe.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sportodemoapp.FatFragment;
import com.sportodemoapp.InfoFragment;
import com.sportodemoapp.MapFragment;
import com.sportodemoapp.RatingFragment;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Info fragment activity
            return new InfoFragment();
        case 1:
            // Rating fragment activity
            return new RatingFragment();
        case 2:
            // fat fragment activity
            return new FatFragment();
        case 3:
            // map fragment activity
            return new MapFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
 
}