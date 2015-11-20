package com.codepath.apps.DoGether.adapters;

/**
 * Created by rshah4 on 11/19/15.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
