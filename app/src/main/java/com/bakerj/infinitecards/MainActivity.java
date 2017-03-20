package com.bakerj.infinitecards;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;

import com.bakerj.infinitecards.transformer.DefaultCommonTransformer;
import com.bakerj.infinitecards.transformer.DefaultTransformerToBack;
import com.bakerj.infinitecards.transformer.DefaultTransformerToFront;
import com.bakerj.infinitecards.transformer.DefaultZIndexTransformerCommon;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private InfiniteCardView mCardView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCardView = (InfiniteCardView) findViewById(R.id.view);
        mAdapter = new MyAdapter();
        mAdapter.addColor(Color.WHITE);
        mAdapter.addColor(Color.RED);
        mAdapter.addColor(Color.BLACK);
        mAdapter.addColor(Color.YELLOW);
        mAdapter.addColor(Color.CYAN);
        mCardView.setAdapter(mAdapter);
        initButton();
    }

    private void initButton() {
        findViewById(R.id.style1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStyle1();
            }
        });
        findViewById(R.id.style2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStyle2();
            }
        });
        findViewById(R.id.style3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStyle3();
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                mAdapter.addColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                mAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.removeColor();
                mAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                mAdapter.changeColor(random.nextInt(mAdapter.getCount()),
                        Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                mAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void setStyle1() {
        mCardView.setClickable(true);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT);
        mCardView.setAnimInterpolator(new LinearInterpolator());
        mCardView.setTransformerToFront(new DefaultTransformerToFront());
        mCardView.setTransformerToBack(new DefaultTransformerToBack());
        mCardView.setZIndexTransformerToBack(new DefaultZIndexTransformerCommon());
    }

    private void setStyle2() {
        mCardView.setClickable(true);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_SWITCH);
        mCardView.setAnimInterpolator(new OvershootInterpolator(-18));
        mCardView.setTransformerToFront(new DefaultTransformerToFront());
        mCardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                ViewHelper.setScaleX(view, (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount));
                ViewHelper.setScaleY(view, (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount));
                if (fraction < 0.5) {
                    ViewCompat.setRotationX(view, 180 * fraction);
                } else {
                    ViewCompat.setRotationX(view, 180 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                ViewHelper.setTranslationY(view, -cardHeight * 0.1f * fromPosition + cardHeight
                        * 0.1f * fraction * (fromPosition - toPosition));
            }
        });
        mCardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.4f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
    }

    private void setStyle3() {
        mCardView.setClickable(false);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT_TO_LAST);
        mCardView.setAnimInterpolator(new OvershootInterpolator(-8));
        mCardView.setTransformerToFront(new DefaultCommonTransformer());
        mCardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                ViewHelper.setScaleX(view, (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount));
                ViewHelper.setScaleY(view, (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount));
                if (fraction < 0.5) {
                    ViewCompat.setTranslationX(view, cardWidth * fraction * 1.5f);
                    ViewCompat.setRotationY(view, -45 * fraction);
                } else {
                    ViewCompat.setTranslationX(view, cardWidth * 1.5f * (1f - fraction));
                    ViewCompat.setRotationY(view, -45 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                ViewHelper.setTranslationY(view, -cardHeight * 0.1f * fromPosition + cardHeight
                        * 0.1f * fraction * (fromPosition - toPosition));
            }
        });
        mCardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.5f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
    }

    private void next() {
        mCardView.bringCardToFront(1);
    }

    private static class MyAdapter extends BaseAdapter {
        private List<Integer> colors = new ArrayList<>();

        @Override
        public int getCount() {
            return colors.size();
        }

        @Override
        public Integer getItem(int position) {
            return colors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_card, parent, false);
            }
            convertView.setBackgroundColor(colors.get(position));
            return convertView;
        }

        void addColor(int color) {
            colors.add(color);
        }

        void removeColor() {
            if (colors.size() > 0) {
                colors.remove(0);
            }
        }

        void changeColor(int position, int color) {
            if (colors.size() > position) {
                colors.set(position, color);
            }
        }
    }
}
