package com.example.madara.parkino;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.madara.parkino.adapters.CardAdapter;
import com.example.madara.parkino.dialogs.GetPassword;
import com.example.madara.parkino.models.Card;
import com.example.madara.parkino.models.CardResponse;
import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCardsFragment extends Fragment implements GetPassword.PasswordListener {
    private static final String TAG = "UserCards";
    private RecyclerView recyclerView;
    private FloatingActionButton bindCard;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Call<MainResponse> mBindCardCall;
    private String mBarcode;
    private String mUserPassword;
    private CardAdapter cardAdapter;
    private List<CardResponse> cards;
    private Call<List<CardResponse>> getCardsCall;
    private Call<MainResponse> removeCardCall;
    private int position;
    public static final int REQUEST_CODE = 100;

    public UserCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_cards, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_card);
        bindCard = (FloatingActionButton) view.findViewById(R.id.bind_card);
        progressBar = (ProgressBar) view.findViewById(R.id.cards_progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.cards_refresh);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cards = new ArrayList<CardResponse>();
        cardAdapter = new CardAdapter(cards, getActivity());
        recyclerView.setAdapter(cardAdapter);
        getCards();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipChatRoomCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        bindCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QRScanner.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getCards();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                mBarcode = barcode.displayValue;
                GetPassword getPassword = new GetPassword();
                Bundle args = new Bundle();
                args.putInt("num", 1);
                getPassword.setArguments(args);
                getPassword.setTargetFragment(UserCardsFragment.this, 100);
                getPassword.show(getFragmentManager(), "GetPassword");
                //getPassword.show();

            }
        }
    }

    private void bindNewCard(String password) {
        if (password.isEmpty()) {
            cardAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Empty Password !", Toast.LENGTH_SHORT).show();
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            mUserPassword = password;
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Binding...");
            progressDialog.show();
            final Card card = new Card();
            card.id = Session.getInstance().getUser().id;
            card.password = mUserPassword;
            card.qrcode = mBarcode;
            mBindCardCall = WebService.getInstance().getApi().bindCard(card);
            mBindCardCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if(!mBindCardCall.isCanceled()){
                    try {
                        if (response.body().status == 0) {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        } else if (response.body().status == 1) {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            getCards();

                        } else {
                            progressDialog.cancel();
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }}
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    if(!mBindCardCall.isCanceled()){
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

    private void getCards() {
        progressBar.setVisibility(View.VISIBLE);
        getCardsCall = WebService.getInstance().getApi().getCards(Session.getInstance().getUser().id);
        getCardsCall.enqueue(new Callback<List<CardResponse>>() {
            @Override
            public void onResponse(Call<List<CardResponse>> call, Response<List<CardResponse>> response) {
                if (!getCardsCall.isCanceled()) {
                    try {
                        cards = response.body();
                        //cardAdapter = new CardAdapter(cards, getActivity());
                        recyclerView.setAdapter(cardAdapter);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CardResponse>> call, Throwable t) {
                if (!getCardsCall.isCanceled()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void removeCard(String password) {
        if (password.isEmpty()) {
            Toast.makeText(getActivity(), "Empty Password !", Toast.LENGTH_SHORT).show();
            cardAdapter.notifyDataSetChanged();

            //_recyclerCardView.setAdapter(cardAdapter);
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            mUserPassword = password;
            final Card card = new Card();
            card.id = Session.getInstance().getUser().id;
            card.password = mUserPassword;
            card.qrcode = cards.get(position).getId();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Unbinding...");
            progressDialog.show();
            //cards.remove(position);
            //cardAdapter.notifyItemRemoved(position);
            //Toast.makeText(MyCards.this, "remove"+card.qrcode, Toast.LENGTH_LONG).show();

            // start Retrofit call
            removeCardCall = WebService.getInstance().getApi().unbindcard(card);
            removeCardCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (!removeCardCall.isCanceled()) {
                        try {
                            if (response.body().status == 1) {
                                // toast message result
                                progressDialog.cancel();
                                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                                // delete message from local chat room list which showed on adapter  now
                                cards.remove(position);
                                // notify adapter that chat room deleted so its delete it
                                cardAdapter.notifyItemRemoved(position);

                            } else {
                                // toast message if status 0 it will be error
                                progressDialog.cancel();
                                cardAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                            cardAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    // toast message of fail
                    if (!removeCardCall.isCanceled()) {
                        progressDialog.cancel();
                        cardAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    ItemTouchHelper.SimpleCallback swipChatRoomCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // get adapter position
            position = viewHolder.getAdapterPosition();
            // get chat room id from chat rooms list depedning on position
            //int cardID = Integer.parseInt(cards.get(position).getId());
            GetPassword getPassword = new GetPassword();
            Bundle args = new Bundle();
            args.putInt("num", 0);
            getPassword.setArguments(args);
            getPassword.setTargetFragment(UserCardsFragment.this, 200);
            getPassword.show(getFragmentManager(), "GetPassword");
        }


        // to color the background of swiped item red color
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // if swiping now
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // get item that swiped
                View itemView = viewHolder.itemView;
                // create new pain
                Paint p = new Paint();
                // if swiping to left dx will < 0 so we do what we want
                if (dX >= 0) {
                    //Log.e(TAG,"yes");
                    // set color for paint red color
                    p.setColor(Color.parseColor("#EBECED"));
                    // draw rectangle depending on the item view ends
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(),
                            (float) itemView.getLeft() + dX, (float) itemView.getBottom(), p);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

    };


    @Override
    public void getPassword(String password, int key) {
        if (key == 1) {
            bindNewCard(password);
        } else if (key == 0) {
            removeCard(password);
        }
    }

    @Override
    public void cancelPassword() {
        cardAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("My Cards");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBindCardCall != null) {
            mBindCardCall.cancel();
        } else if (getCardsCall != null) {
            getCardsCall.cancel();
        } else if (removeCardCall != null) {
            removeCardCall.cancel();
        }
    }
}
