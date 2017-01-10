package tonnysunm.com.acornote.ui.flip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.databinding.FlipPageFragmentBinding;
import tonnysunm.com.acornote.model.Item;

/**
 * Created by Tonny on 31/12/16.
 */

public class ItemPageAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = ItemPageAdapter.class.getSimpleName();

    private final List<Item> mDataSource;

    ItemPageAdapter(FragmentManager fm) {
        super(fm);
        mDataSource = new ArrayList<Item>();
    }

    @Override
    public int getCount() {
        Log.d(TAG, String.format("getCount %d", mDataSource.size()));
        return mDataSource.size();
    }

    public void setDataSource(List<Item> datas){
        mDataSource.clear(); //GC
        mDataSource.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return new ItemViewPageFragment(mDataSource.get(position));
    }

    private static class ItemViewPageFragment extends Fragment {
        private static final String TAG = ItemViewPageFragment.class.getSimpleName();

        private Item mItem;

        ItemViewPageFragment() {
        }

        ItemViewPageFragment(Item item) {
            Log.d(TAG, "ItemViewPageFragment");
            mItem = item;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.flip_page_fragment, container, false);

            FlipPageFragmentBinding binding = FlipPageFragmentBinding.bind(view);
            binding.setViewModel(new FlipItemViewModel(mItem));

            Log.d(TAG, "binding");
            return view;
        }
    }
}
