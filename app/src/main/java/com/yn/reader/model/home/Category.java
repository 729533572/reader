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

package com.yn.reader.model.home;

import android.support.annotation.NonNull;

/**
 * @author drakeet
 */
public class Category {
    private int current_page = 1;
    public @NonNull
    String title;

    public @NonNull
    String subTitle;

    private int home_channel_id;
    private int home_channel_type;
    public Category(@NonNull final String title, String subTitle, int home_channel_id,int home_channel_type) {
        this.title = title;
        this.subTitle = subTitle;
        this.home_channel_id = home_channel_id;
        this.home_channel_type = home_channel_type;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void updateCurrentPage() {
        this.current_page = ++current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(@NonNull String subTitle) {
        this.subTitle = subTitle;
    }

    public int getHome_channel_id() {
        return home_channel_id;
    }

    public void setHome_channel_id(int home_channel_id) {
        this.home_channel_id = home_channel_id;
    }

    public int getHome_channel_type() {
        return home_channel_type;
    }

    public void setHome_channel_type(int home_channel_type) {
        this.home_channel_type = home_channel_type;
    }

}
