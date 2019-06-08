package com.amanpatel.veggiestoretest0.View;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.Category1Adapter;
import com.amanpatel.veggiestoretest0.Adapters.ImageSliderAdapter;
import com.amanpatel.veggiestoretest0.Interface.CustomItemClickListener;
import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.Models.AdminBanner;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductCategory1;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class MarketFrag extends Fragment {
    public ActivityMainBinding binding;
    private MarketViewModel mainactivityviewmodel;
    private List<ProductCategory1> catList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Category1Adapter mAdapter;
    private Context c;
    private FloatingActionButton fab;
    private ViewPager mPager;
    FrameLayout frameLayout;
    RelativeLayout relativeLayoutPBar;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> imagesUrl = new ArrayList<>();
    private Handler handler;
    private Runnable Update;
    SharedPrefs sharedPrefs;
    TextView textCartItemCount;
    int mCartItemCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            getActivity().setTitle("Market");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_market, container, false);
        c = v.getContext();
        sharedPrefs = new SharedPrefs(c);
        fab = v.findViewById(R.id.fab_mfrag);
        frameLayout = v.findViewById(R.id.banner_layout);
        relativeLayoutPBar = v.findViewById(R.id.progress_bar_mfrag);
        frameLayout.setVisibility(View.INVISIBLE);
        relativeLayoutPBar.setVisibility(View.VISIBLE);
        mPager = v.findViewById(R.id.pager_mfrag);

        textCartItemCount = v.findViewById(R.id.cart_badge);

        mPager.addOnPageChangeListener(new MarketFrag.CircularViewPagerHandler(mPager));
        mPager.setAdapter(new ImageSliderAdapter(c, imagesUrl));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = v.findViewById(R.id.recycler_view_mFrag);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        loadData();

        mainactivityviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                toastMaker(s);
                Snackbar.make(fab, R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadData();
                            }
                        }).setActionTextColor(ContextCompat.getColor(c, R.color.snackbarActionColor)).show();
            }
        });
        return v;
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        mainactivityviewmodel.getCart(sharedPrefs.getString(getContext(), CID), sharedPrefs.getString(getContext(), AreaID)).observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> cartList) {
                if(cartList.size()==0){
                    fab.setVisibility(View.INVISIBLE);
                }
                else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        mainactivityviewmodel.getCategory1().observe(this, new Observer<List<ProductCategory1>>() {
            @Override
            public void onChanged(@Nullable List<ProductCategory1> productCategory1s) {
                catList = productCategory1s;
                mAdapter = new Category1Adapter(getActivity(), catList, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        String categoryName = catList.get(position).getName();
                        String category1ID = catList.get(position).getId();
                        Intent intent = new Intent(getActivity(), Products1.class);
                        intent.putExtra("Title", categoryName);
                        intent.putExtra("Category1ID", category1ID);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(mAdapter);
            }
        });
        mainactivityviewmodel.getBanner(sharedPrefs.getString(c, SharedPrefs.AreaID)).observe(this, new Observer<List<AdminBanner>>() {
            @Override
            public void onChanged(List<AdminBanner> adminBanners) {
                if (adminBanners != null) {
                    for (int i = 0; i < adminBanners.size(); i++)
                        imagesUrl.add(adminBanners.get(i).getLink());
                    frameLayout.setVisibility(View.VISIBLE);
                    relativeLayoutPBar.setVisibility(View.INVISIBLE);
                }
                mPager.setAdapter(new ImageSliderAdapter(c, imagesUrl));
                if (mPager.getAdapter() != null) {
                    mPager.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    public void setupBadge() {
        if (sharedPrefs.getString(getContext(), isLoggedIn).equals("true")) {
            mainactivityviewmodel.getCart(sharedPrefs.getString(getContext(), CID), sharedPrefs.getString(getContext(), AreaID)).observe(this, new Observer<List<Cart>>() {
                @Override
                public void onChanged(List<Cart> cartList) {
                    mCartItemCount = cartList.size();
                    if (textCartItemCount != null) {
                        if (mCartItemCount == 0) {
                            if (textCartItemCount.getVisibility() != View.GONE) {
                                textCartItemCount.setVisibility(View.GONE);
                            }
                        } else {
                            textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
        } else {
            mCartItemCount = CartActivity.guestCart.size();
            if (textCartItemCount != null) {
                if (mCartItemCount == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
    public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        private ViewPager mViewPager;
        private int mCurrentPosition;
        private int mScrollState;

        public CircularViewPagerHandler(final ViewPager viewPager) {
            mViewPager = viewPager;

        }

        @Override
        public void onPageSelected(final int position) {
            currentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            handleScrollState(state);
            mScrollState = state;
        }

        private void handleScrollState(final int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                setNextItemIfNeeded();
            }
        }

        private void setNextItemIfNeeded() {
            if (!isScrollStateSettling()) {
                handleSetNextItem();
            }
        }

        private boolean isScrollStateSettling() {
            return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
        }

        private void handleSetNextItem() {
            final int lastPosition = Objects.requireNonNull(mViewPager.getAdapter()).getCount() - 1;
            if (currentPage == 0) {
                mViewPager.setCurrentItem(lastPosition, false);
            } else if (currentPage == lastPosition) {
                mViewPager.setCurrentItem(0, false);
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
