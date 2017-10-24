package caventa.ansheer.ndk.caventa;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent i=new Intent(Home.this,MainActivity.class);
                startActivity(i);
            }
        });

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    static int fin_data_flag = 0, pen_data_flag = 0, up_data_flag = 0;

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Pending_Works_Fragment extends Fragment {


        public Pending_Works_Fragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Pending_Works_Fragment newInstance() {
            Pending_Works_Fragment fragment = new Pending_Works_Fragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pending_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);

            mAdapter = new MoviesAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movieList.get(position);
                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (pen_data_flag == 0) {
                prepareMovieData();
            }

            return rootView;
        }

        private List<Movie> movieList = new ArrayList<>();
        private RecyclerView recyclerView;
        private MoviesAdapter mAdapter;

        private void prepareMovieData() {
            Movie movie = new Movie("Mad Max: Fury Road", "Action & Adventure", "2015");
            movieList.add(movie);

            movie = new Movie("Inside Out", "Animation, Kids & Family", "2015");
            movieList.add(movie);

            movie = new Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
            movieList.add(movie);

            movie = new Movie("Shaun the Sheep", "Animation", "2015");
            movieList.add(movie);

            movie = new Movie("The Martian", "Science Fiction & Fantasy", "2015");
            movieList.add(movie);

            movie = new Movie("Mission: Impossible Rogue Nation", "Action", "2015");
            movieList.add(movie);

            mAdapter.notifyDataSetChanged();

            pen_data_flag=1;
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Finished_Works_Fragment extends Fragment {

        public Finished_Works_Fragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Finished_Works_Fragment newInstance() {
            Finished_Works_Fragment fragment = new Finished_Works_Fragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_finished_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);

            mAdapter = new MoviesAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movieList.get(position);
                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (fin_data_flag == 0) {
                prepareMovieData();
            }

            return rootView;
        }

        private List<Movie> movieList = new ArrayList<>();
        private RecyclerView recyclerView;
        private MoviesAdapter mAdapter;

        private void prepareMovieData() {


            Movie movie = new Movie("Up", "Animation", "2009");
            movieList.add(movie);

            movie = new Movie("Star Trek", "Science Fiction", "2009");
            movieList.add(movie);

            movie = new Movie("The LEGO Movie", "Animation", "2014");
            movieList.add(movie);

            movie = new Movie("Iron Man", "Action & Adventure", "2008");
            movieList.add(movie);

            movie = new Movie("Aliens", "Science Fiction", "1986");
            movieList.add(movie);


            mAdapter.notifyDataSetChanged();

            fin_data_flag=1;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Upcoming_Works_Fragment extends Fragment {

        public Upcoming_Works_Fragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Upcoming_Works_Fragment newInstance() {
            Upcoming_Works_Fragment fragment = new Upcoming_Works_Fragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_upcoming_works, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);

            mAdapter = new MoviesAdapter(movieList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movieList.get(position);
                    Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            if (up_data_flag == 0) {
                prepareMovieData();
            }

            return rootView;
        }

        private List<Movie> movieList = new ArrayList<>();
        private RecyclerView recyclerView;
        private MoviesAdapter mAdapter;

        private void prepareMovieData() {


            Movie movie = new Movie("Chicken Run", "Animation", "2000");
            movieList.add(movie);

            movie = new Movie("Back to the Future", "Science Fiction", "1985");
            movieList.add(movie);

            movie = new Movie("Raiders of the Lost Ark", "Action & Adventure", "1981");
            movieList.add(movie);

            movie = new Movie("Goldfinger", "Action & Adventure", "1965");
            movieList.add(movie);

            movie = new Movie("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
            movieList.add(movie);

            mAdapter.notifyDataSetChanged();

            up_data_flag=1;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a Pending_Works_Fragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return Pending_Works_Fragment.newInstance();

                case 1:
                    return Finished_Works_Fragment.newInstance();

                default:
                    return Upcoming_Works_Fragment.newInstance();


            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Fin";
                case 1:
                    return "Pen";
                case 2:
                    return "Up";
            }
            return null;
        }
    }
}
