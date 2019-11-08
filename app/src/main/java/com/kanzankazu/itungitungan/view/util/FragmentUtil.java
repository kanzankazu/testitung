package com.kanzankazu.itungitungan.view.util;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kanzankazu.itungitungan.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentUtil {

    private AppCompatActivity mActivity;
    private int targetView;

    private Fragment currentFragment;

    private Toolbar stepToolbar;
    private Fragment[] stepFragments;
    private Fragment stepCurrentFragment;

    public FragmentUtil(AppCompatActivity mActivity, @IdRes int targetView) {
        this.mActivity = mActivity;
        this.targetView = targetView;
    }

    public FragmentUtil(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    private FragmentTransaction getTransaction() {
        return mActivity.getSupportFragmentManager().beginTransaction();
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getTransaction()
                    .replace(targetView, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public static List<Fragment> convertFragmentArrayToListFragment(Fragment[] fragments) {
        List<Fragment> fragmentList = new ArrayList<>();
        Collections.addAll(fragmentList, fragments);
        /*for (int i = 0; i < fragments.length; i++) {
            arrayList.add(fragments[i]);
        }*/
        return fragmentList;
    }

    public static List<String> convertStringArrayToListString(String[] strings) {
        List<String> stringList = new ArrayList<>();
        Collections.addAll(stringList, strings);
        /*for (int i = 0; i < fragments.length; i++) {
            arrayList.add(fragments[i]);
        }*/
        return stringList;
    }

    /**
     * STATIC
     */
    public Fragment addFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = getTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.add(targetView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.commit();
        return fragment;
    }

    public Fragment addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getTransaction();
        fragmentTransaction.add(targetView, fragment);
        fragmentTransaction.commit();
        return fragment;
    }

    public void addFragments(Fragment... fragments) {
        FragmentTransaction ft = getTransaction();
        for (Fragment targetClass : fragments) {
            ft.add(targetView, targetClass);
        }
        ft.commit();
    }

    public void addshowHideFragments(Fragment[] fragments) {
        FragmentTransaction ft = getTransaction();
        for (int i = 0; i < fragments.length; i++) {
            ft.add(targetView, fragments[i]);
            if (i == 0) {
                ft.show(fragments[i]);
                currentFragment = fragments[i];
            } else {
                ft.hide(fragments[i]);
            }
        }
        ft.commit();
    }

    public void showHideFragment(Fragment showFragment, Fragment hideFragment) {
        FragmentTransaction ft = getTransaction();
        ft.show(showFragment);
        currentFragment = showFragment;
        ft.hide(hideFragment);
        ft.commit();
    }

    public void replaceFragmentBackStack(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getTransaction();
        transaction.replace(targetView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getTransaction();
        transaction.replace(targetView, fragment);
        transaction.commit();
    }

    /**
     * BottomNavigationView
     */
    public void setupBottomNavigationView(BottomNavigationView bottomNavigationView, int[] idMenus, Fragment fragment, Boolean isReplace, Fragment... fragments) {
        if (idMenus.length != fragments.length) {
            mActivity.finishAffinity();
            Toast.makeText(getApplicationContext(), "Menu dan Fragment tidak sama", Toast.LENGTH_SHORT).show();
        } else {
            if (isReplace) {
                initFirstTab(fragment);
                initListener(bottomNavigationView, idMenus, fragments);
            } else {
                initFirstTabShowHide(fragments);
                initListenershowHide(bottomNavigationView, idMenus, fragments);
            }
        }
    }

    private void initFirstTab(Fragment fragment) {
        replaceFragment(fragment);
    }

    private void initListener(BottomNavigationView bottomNavigationView, int[] idMenus, Fragment... fragments) {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;

            for (int i = 0; i < idMenus.length; i++) {
                int idMenu = idMenus[i];

                if (menuItem.getItemId() == idMenu) {
                    selectedFragment = fragments[i];
                }
            }

            replaceFragment(selectedFragment);

            return true;
        });
    }

    private void initFirstTabShowHide(Fragment[] fragment) {
        addshowHideFragments(fragment);
    }

    private void initListenershowHide(BottomNavigationView bottomNavigationView, int[] idMenus, Fragment... fragments) {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;

            for (int i = 0; i < idMenus.length; i++) {
                int idMenu = idMenus[i];

                if (menuItem.getItemId() == idMenu) {
                    selectedFragment = fragments[i];
                }
            }

            if (selectedFragment != currentFragment)
                showHideFragment(selectedFragment, currentFragment);

            return true;
        });
    }

    /**
     * ViewPager TabLayout
     */
    public SlidePagerAdapter setupTabLayoutViewPager(String[] titleTab,@Nullable int[] iconTab, TabLayout tabLayout, ViewPager viewPager, Fragment... fragments) {

        SlidePagerAdapter mPagerAdapter = new SlidePagerAdapter(mActivity.getSupportFragmentManager());
        mPagerAdapter.addFragments(convertFragmentArrayToListFragment(fragments), convertStringArrayToListString(titleTab));

        if (iconTab != null) {
            for (int i = 0; i < iconTab.length; i++) {
                tabLayout.addTab(tabLayout.newTab().setIcon(iconTab[i]));
                if (i == 0) {
                    tabLayout.getTabAt(0).getIcon().setColorFilter(mActivity.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                } else {
                    tabLayout.getTabAt(i).getIcon().setColorFilter(mActivity.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                }
            }
        }

        viewPager.setAdapter(mPagerAdapter);
        int limit = (mPagerAdapter.getCount() > 1 ? mPagerAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
        tabLayout.setupWithViewPager(viewPager);

        return mPagerAdapter;
    }

    public class SlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles;

        SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String string) {
            fragments.add(fragment);
            titles.add(string);
            notifyDataSetChanged();
        }

        public void addFragments(List<Fragment> fragments, List<String> strings) {
            this.titles = strings;
            if (this.fragments.size() > 0) {
                this.fragments.clear();
                this.fragments = fragments;
            } else {
                this.fragments = fragments;
            }
            notifyDataSetChanged();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        /*public Fragment[] add(Fragment[] originalArray, Fragment newItem) {
            int currentSize = originalArray.length;
            int newSize = currentSize + 1;
            Fragment[] tempArray = new Fragment[newSize];
            for (int i = 0; i < currentSize; i++) {
                tempArray[i] = originalArray[i];
            }
            //System.arraycopy(originalArray, 0, tempArray, 0, currentSize);
            tempArray[newSize - 1] = newItem;
            return tempArray;
        }*/
    }

    public SlidePagerAdapterInfinite setupTabLayoutViewPagerInfinite(TabLayout tabLayout, ViewPager viewPager, Fragment... fragments) {

        SlidePagerAdapterInfinite mPagerAdapter = new SlidePagerAdapterInfinite(mActivity.getSupportFragmentManager(), convertFragmentArrayToListFragment(fragments));

        viewPager.setAdapter(mPagerAdapter);
        int limit = (mPagerAdapter.getCount() > 1 ? mPagerAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);

        return mPagerAdapter;
    }

    public class SlidePagerAdapterInfinite extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public SlidePagerAdapterInfinite(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return getFragmentBasedOnPosition(position);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        private Fragment getFragmentBasedOnPosition(int position) {
            int fragmentPos = position % fragments.size(); // Assuming you have 3 fragments

            Fragment selectedFragment = null;

            for (int i = 0; i < fragments.size(); i++) {

                if (fragmentPos == i + 1) {
                    selectedFragment = fragments.get(i);
                }
            }

            return selectedFragment;
        }
    }

    /**
     * Step Fragment
     */
    public void setupStepFragment(AppCompatActivity mActivity, @IdRes int targetView, Toolbar stepToolbar, Fragment... stepFragments) {
        if (stepFragments.length > 0) {
            this.mActivity = mActivity;
            this.targetView = targetView;
            this.stepToolbar = stepToolbar;
            this.stepFragments = stepFragments;

            initStepToolbarPreApproval(1);

            FragmentTransaction ft = getTransaction();
            for (int i = 0; i < stepFragments.length; i++) {
                Fragment fragment = stepFragments[i];
                ft.add(targetView, fragment);

                if (i == 0) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        } else {
            Snackbar.make(mActivity.findViewById(android.R.id.content), "Fragment tidak ada", Snackbar.LENGTH_SHORT).show();
        }

    }

    public void stepToolbarUpdate(int step) {
        initStepToolbarPreApproval(step);
    }

    public void initStepToolbarPreApproval(int step) {
        int minNormalStep = step;
        int maxNormalStep = stepFragments.length + 1;

        mActivity.setSupportActionBar(stepToolbar);
        mActivity.getSupportActionBar().setTitle("");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stepToolbar.setNavigationOnClickListener(view -> mActivity.onBackPressed());

        initViewStepToolBar();
    }

    private void initViewStepToolBar() {
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(mActivity);
        ImageView imageView = new ImageView(mActivity);
    }

    //method ini di dalam onBackPressed Activity
    public void stepFragmentBackPressed() {
        if (stepFragments.length > 0) {
            for (int i = 0; i < stepFragments.length; i++) {
                Fragment fragment = stepFragments[i];
                if (fragment.isVisible()) {
                    stepCurrentFragment = fragment;
                    stepToolbarUpdate(i);
                }
            }
        }
    }

}
