package com.yn.reader.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yn.reader.model.common.Book;
import com.yn.reader.view.BookListActivity;
import com.yn.reader.view.DetailActivity;
import com.yn.reader.view.ReaderActivity;

/**
 * Created by sunxy on 2018/2/22.
 */

public class IntentUtils {
    public static void startBookListActivity(Context context, int fragmentType, String fragmentTitle, int categoryId, int categorySex) {
        Intent intent = new Intent(context, BookListActivity.class);
        intent.putExtra(Constant.KEY_FRAGMENT_TYPE, fragmentType);
        intent.putExtra(Constant.KEY_FRAGMENT_TITLE, fragmentTitle);
        intent.putExtra(Constant.KEY_CATEGORY_ID, categoryId);
        intent.putExtra(Constant.KEY_CATEGORY_SEX, categorySex);
        context.startActivity(intent);
    }

    public static void startBookDetailActivity(Context context, long bookId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constant.KEY_BOOK_ID, bookId);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, long id) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constant.KEY_ID, id);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, int type) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constant.KEY_TYPE, type);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, String keyword) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constant.KEY_WORD, keyword);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, String keyword1, String keyword2) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constant.KEY_WORD, keyword1);
        intent.putExtra(Constant.KEY_WORD_ANTHER, keyword2);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void startActivityAndFinish(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void popPreviousActivity(Activity activity) {
        activity.finish();
    }

    public static void startReadActivity(Context context, Book bookShelf) {
        //进入阅读
        Intent intent = new Intent(context, ReaderActivity.class);
        String key = String.valueOf(System.currentTimeMillis());
        intent.putExtra("data_key", key);
        BitIntentDataManager.getInstance().putData(key, bookShelf);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, long id, int value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constant.KEY_ID, id);
        intent.putExtra(Constant.KEY_INT, value);
        context.startActivity(intent);
    }
}
