package sporto1.tabswipe.adapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sportodemoapp.FatFragment;
import com.sportodemoapp.InfoFragment;
import com.sportodemoapp.RatingFragment;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
	public String compositeKey;
 
    public TabsPagerAdapter(FragmentManager fm, String compositekey) {
    	super(fm);
    	compositeKey = compositekey;
            }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Info fragment activity
			Bundle data = new Bundle();
			data.putString("compositekey",compositeKey);
			InfoFragment fragment = new InfoFragment();
			fragment.setArguments(data);
            return fragment;
        case 1:
            // Rating fragment activity
			Bundle data1 = new Bundle();
			data1.putString("compositekey",compositeKey);
			RatingFragment fragment1 = new RatingFragment();
			fragment1.setArguments(data1);
            return fragment1;            
        case 2:
            // fat fragment activity
            return new FatFragment();

        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}