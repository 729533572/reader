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

import com.yn.reader.model.home.TopBanner;

import me.drakeet.multitype.ItemViewBinder;


/**
 * @author drakeet
 */
public class TopBannerItemViewBinder extends ItemViewBinder<TopBanner, TopBannerItemViewBinder.ViewHolder> {
  private View topBannerView;
  public TopBannerItemViewBinder(View topBannerView){
    this.topBannerView = topBannerView;
  }
  @Override
  protected @NonNull ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
    return new ViewHolder(this.topBannerView);
  }


  @Override
  protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TopBanner topBanner) {
//    holder.title.setText(category.title);
  }


  static class ViewHolder extends RecyclerView.ViewHolder {

//    private @NonNull final TextView title;


    ViewHolder(@NonNull View itemView) {
      super(itemView);
//      title = itemView.findViewById(R.id.title);
    }
  }
}
