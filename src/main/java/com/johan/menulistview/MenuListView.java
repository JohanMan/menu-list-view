package com.johan.menulistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */

public class MenuListView extends ListView implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, View.OnClickListener {

    private MenuListViewItemLayout lastSlideLayout;
    private int lastSlidePosition;
    private int lastFirst;
    private View firstVisibleView, lastVisibleView;
    private int menuViewsWidth;
    private OnItemMenuClickListener onItemMenuClickListener;
    private List<ItemMenu> itemMenuList = new ArrayList<>();

    public MenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemClickListener(this);
        setOnScrollListener(this);
    }

    public void addItemMenu(ItemMenu itemMenu) {
        itemMenuList.add(itemMenu);
    }

    public void refresh() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                for (ItemMenu menu : itemMenuList) {
                    menuViewsWidth += menu.getWidth();
                    for (int i = 0; i < getChildCount(); i++) {
                        View childView = getChildAt(i);
                        if (childView instanceof MenuListViewItemLayout) {
                            ((MenuListViewItemLayout) childView).addView(buildSlideMenuView(menu));
                        }
                    }
                }
            }
        }, 200);
    }

    private RelativeLayout buildSlideMenuView(ItemMenu itemMenu) {
        RelativeLayout menuLayout = new RelativeLayout(getContext());
        menuLayout.setTag(itemMenu.getId());
        menuLayout.setBackgroundResource(itemMenu.getBackground());
        TextView textView = new TextView(getContext());
        textView.setText(itemMenu.getText());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemMenu.getTextSize());
        textView.setTextColor(itemMenu.getTextColor());
        RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        menuLayout.addView(textView, textLayoutParams);
        ImageView iconView = new ImageView(getContext());
        iconView.setImageResource(itemMenu.getIcon());
        int iconSize = itemMenu.getIconSize() == 0 ? RelativeLayout.LayoutParams.WRAP_CONTENT : itemMenu.getIconSize();
        RelativeLayout.LayoutParams iconLayoutParams = new RelativeLayout.LayoutParams(iconSize, iconSize);
        iconLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        menuLayout.addView(iconView, iconLayoutParams);
        menuLayout.setOnClickListener(this);
        MenuListViewItemLayout.LayoutParams params = new MenuListViewItemLayout.LayoutParams(itemMenu.getWidth(), MenuListViewItemLayout.LayoutParams.MATCH_PARENT);
        menuLayout.setLayoutParams(params);
        return menuLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view instanceof MenuListViewItemLayout) {
            MenuListViewItemLayout layout = (MenuListViewItemLayout) view;
            boolean slideState = slide(layout, false);
            if (!slideState) return;
            if (lastSlideLayout != null && lastSlideLayout != layout) {
                slide(lastSlideLayout, false);
            }
            lastSlideLayout = layout;
            lastSlidePosition = position;
        }
    }

    private boolean slide(final MenuListViewItemLayout layout, boolean isForceClose) {
        boolean viewState = false;
        if (layout.getTag(R.id.tag_slide) != null) {
            viewState = (Boolean) layout.getTag(R.id.tag_slide);
        }
        if (isForceClose && !viewState) {
            return false;
        }
        int startX = viewState ? -menuViewsWidth : 0;
        int endX = viewState ? 0 : -menuViewsWidth;
        if (isForceClose) {
            layout.scrollTo(0, 0);
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(startX, endX);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int x = (int) animation.getAnimatedValue();
                    layout.scrollTo(-x, 0);
                }
            });
            animator.setDuration(500);
            animator.start();
        }
        layout.setTag(R.id.tag_slide, !viewState);
        return !viewState;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem != lastFirst) {
            if (firstVisibleItem > lastFirst) {
                forceClose(firstVisibleView);
            } else {
                forceClose(lastVisibleView);
            }
        }
        firstVisibleView = getChildAt(0);
        lastVisibleView = getChildAt(visibleItemCount - 1);
        lastFirst = firstVisibleItem;
    }

    private void forceClose(View view) {
        if (view instanceof MenuListViewItemLayout) {
            MenuListViewItemLayout layout = (MenuListViewItemLayout) view;
            slide(layout, true);
        }
    }

    @Override
    public void onClick(View view) {
        int id = (int) view.getTag();
        MenuListViewItemLayout layout = (MenuListViewItemLayout) getChildAt(lastSlidePosition - lastFirst);
        slide(layout, false);
        if (onItemMenuClickListener != null) {
            onItemMenuClickListener.onItemMenuClick(id, lastSlidePosition);
        }
    }

    public void setOnItemMenuClickListener(OnItemMenuClickListener onItemMenuClickListener) {
        this.onItemMenuClickListener = onItemMenuClickListener;
    }

    public interface OnItemMenuClickListener {
        void onItemMenuClick(int id, int position);
    }

}
