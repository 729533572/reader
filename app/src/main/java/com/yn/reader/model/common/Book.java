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

package com.yn.reader.model.common;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.searchResult.SearchResultBook;
import com.yn.reader.util.LogUtils;
import com.yn.reader.widget.BookContentView;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author drakeet
 */
@Entity
public class Book {
    @Id
    private long bookid;

    private String bookname;

    private String bookauthor;

    private String bookimage;

    private String bookdesc;

    private String bookprogress;//当前书本进度

    private String chapterprogress;//当前章节进度

    private int isupdate;
    @Transient
    private List<String> booktags;

    private String noteUrl; //对应BookInfoBean noteUrl;

    private long chapterid;   //当前章节 （包括番外）

    private int durChapterPage = BookContentView.DURPAGEINDEXBEGIN;  // 当前章节位置   用页码

    private long finalDate;  //最后阅读时间

    private String tag;


    private int alreadyjoin;//是否已加入收藏,0,无；1，是

    private boolean isHistory;//是否已加入历史

    private boolean isSynchronized;//收藏条件下是否已经上传同步

    @Transient
    private List<ChapterListBean> chapterlist = new ArrayList<>();    //章节列表

    @Transient
    private boolean isSelect;

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public boolean getSynchronized() {
        return isSynchronized;
    }

    public Book(SearchResultBook resultBook) {
        this.bookid = resultBook.getBookid();
        this.bookname = resultBook.getBookname();
        this.bookauthor = resultBook.getBookauthor();
        this.bookimage = resultBook.getBookimage();
        this.bookdesc = resultBook.getBookdesc();
        this.booktags = new ArrayList<>();
        for (int i = 0; i < resultBook.getBooktags().length; i++) {
            String tag = resultBook.getBooktags()[i];
            booktags.add(tag);
        }
    }

    @Generated(hash = 637398197)
    public Book(long bookid, String bookname, String bookauthor, String bookimage, String bookdesc,
                String bookprogress, String chapterprogress, int isupdate, String noteUrl, long chapterid,
                int durChapterPage, long finalDate, String tag, int alreadyjoin, boolean isHistory,
                boolean isSynchronized) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.bookimage = bookimage;
        this.bookdesc = bookdesc;
        this.bookprogress = bookprogress;
        this.chapterprogress = chapterprogress;
        this.isupdate = isupdate;
        this.noteUrl = noteUrl;
        this.chapterid = chapterid;
        this.durChapterPage = durChapterPage;
        this.finalDate = finalDate;
        this.tag = tag;
        this.alreadyjoin = alreadyjoin;
        this.isHistory = isHistory;
        this.isSynchronized = isSynchronized;
    }

    @Generated(hash = 1839243756)
    public Book() {
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public int getDurChapterPage() {
        return durChapterPage;
    }

    public void setDurChapterPage(int durChapterPage, int totalage) {
        this.durChapterPage = durChapterPage;
        if (totalage > 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            setChapterprogress(df.format((float) ((durChapterPage + 1) * 100) / totalage));//乘以100是后台需要的百分比
        }
    }

    public long getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(long finalDate) {
        this.finalDate = finalDate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public List<ChapterListBean> getChapterlist() {
        synchronized (this) {
            if (chapterlist == null || chapterlist.size() < 1) {
                if (chapterlist == null) chapterlist = new ArrayList<>();
                List<ChapterListBean> chapterListBeans = BookDBManager.getInstance().getChapterList(bookid);
                chapterlist.clear();
                chapterlist.addAll(chapterListBeans);
            }
        }
        return chapterlist;
    }

    public ChapterListBean getCurrentChapter() {
        return BookDBManager.getInstance().getBookCurrentChapter(chapterid);
    }

    public void setChapterlist(List<ChapterListBean> chapterlist) {
        this.chapterlist = chapterlist;
    }

    /*************************************************************/

    public String getBookdesc() {
        return bookdesc;
    }

    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }

    public long getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
        setNoteUrl(String.valueOf(bookid));
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookimage() {
        String url = bookimage;
        if (url.startsWith("//")) url = "http:" + url;
        return url;
    }

    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public List<String> getBooktags() {
        return booktags;
    }

    public void setBooktags(List<String> booktags) {
        this.booktags = booktags;
        if (this.booktags != null && this.booktags.size() > 3) {
            this.booktags = this.booktags.subList(0, 3);
        }
    }

    public long getChapterid() {
        return chapterid;
    }

    public void setChapterid(long chapterid) {
        this.chapterid = chapterid;
    }

    public String getBookprogress() {
        return bookprogress;
    }

    public void setBookprogress(String bookprogress) {
        this.bookprogress = bookprogress;

    }

    public String getChapterprogress() {
        return chapterprogress;
    }

    public void setChapterprogress(String chapterprogress) {
        this.chapterprogress = chapterprogress;
        LogUtils.log("bookprogress=" + bookprogress);
    }

    public int getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(int isupdate) {
        this.isupdate = isupdate;
    }


    public void setDurChapterPage(int durChapterPage) {
        this.durChapterPage = durChapterPage;
    }

    public void setCollected(boolean collected) {
        this.alreadyjoin = collected ? 1 : 0;
    }

    public boolean isCollected() {
        return alreadyjoin == 1;
    }

    public boolean getIsHistory() {
        return this.isHistory;
    }

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public void setAlreadyjoin(int alreadyjoin) {
        this.alreadyjoin = alreadyjoin;
    }

    public int getAlreadyjoin() {
        return alreadyjoin;
    }

    public boolean getIsSynchronized() {
        return this.isSynchronized;
    }

    public void setIsSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public int getDurChapterIndex() {
        int durChapterIndex = 0;
        try {
            synchronized (this) {
                List<ChapterListBean> chapterListBeans = BookDBManager.getInstance().getChapterList(bookid);
                ChapterListBean chapterListBean = getCurrentChapter();
                for (int i = 0; i < chapterListBeans.size(); i++) {
                    if (chapterListBeans.get(i).getChapterid() == chapterListBean.getChapterid()) {
                        durChapterIndex = i;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            LogUtil.i(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }
        return durChapterIndex;
    }

    public String getChapterName(int chapterIndex) {
        String durChapterName = "";
        try {
            synchronized (this) {
                List<ChapterListBean> chapterListBeans = BookDBManager.getInstance().getChapterList(bookid);
                durChapterName = chapterListBeans.get(chapterIndex).getChaptername();
            }
        } catch (Exception ex) {
            LogUtil.i(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }
        return durChapterName;
    }
}
