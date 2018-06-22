/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yn.reader.view.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yn.reader.R;
import com.yn.reader.model.home.Divider;

import me.drakeet.multitype.ItemViewBinder;


/**
 * @author drakeet
 */
public class DividerItemViewBinder extends ItemViewBinder<Divider, DividerItemViewBinder.ViewHolder> {
    private int divider_width;

    public DividerItemViewBinder(int width) {
        divider_width = width;
    }

    @Override
    protected @NonNull
    ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_divider, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Divider divider) {

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.getLayoutParams().width = divider_width;
        }
    }
}
